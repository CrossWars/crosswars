import { Group } from '../Groups/groups'
import { getGroupsByJWT } from '../Groups/groups.api'
import { User } from '../Users/users'
import { getUsersByGroupId } from '../Users/users.api'
import { CombinedLeaderboardEntry, Entry, LeaderboardEntry } from './entries'
import { getEntriesByGroupId } from './entries.api'


export async function createDailyCombinedLeaderboardEntries(): Promise<CombinedLeaderboardEntry[]>
{
    const groups = await getGroupsByJWT()
    const promises: Promise<void>[] = []
    const groupToEntries = new Map<Group, Entry[]>();
    const userIdToUsers = new Map<string, User>();
    // populate groupIdToEntries
    for(const group of groups)
    {
        const promise1 = getEntriesByGroupId(group.id).then(
            (entries) => {
                groupToEntries.set(group, entries)
            }
        )
        const promise2 = getUsersByGroupId(group.id).then(
            (users) => {
                for(const user of users)
                {
                    userIdToUsers.set(user.id, user)
                }
            }
        )
        promises.push(promise1)
        promises.push(promise2)
    }
    await Promise.all(promises)
    //create leaderboard entries
    const userIdToCombinedLeaderboardEntry = new Map<string, CombinedLeaderboardEntry>()
    for(const [group, entries] of groupToEntries)
    {
        for(const entry of entries)
        {
            if(!userIdToCombinedLeaderboardEntry.has(entry.user_id))
            {
                //const user = await getUserByUserId(entry.user_id)
                const user = userIdToUsers.get(entry.user_id)
                if(!user)
                {
                    continue;
                }
                userIdToCombinedLeaderboardEntry.set(entry.user_id, {...entry, position: 0, user: user, groups: [group]})
            }
            else
            {
                userIdToCombinedLeaderboardEntry.get(entry.user_id)?.groups.push(group)
            }
        }
    }
    const entries =  Array.from(userIdToCombinedLeaderboardEntry.values())
    setLeaderboardEntryPositions(entries)
    return entries
}
export async function createDailyLeaderboardEntries(group_id: string): Promise<LeaderboardEntry[]>
{
    const entries = await getEntriesByGroupId(group_id)
    const userIdToUsers = await getUsersByGroupId(group_id).then(
        (users) => {
            const userIdToUsers = new Map<string, User>();
            for (const user of users)
            {
                userIdToUsers.set(user.id, user)
            }
            return userIdToUsers;
        }
    )
    const leaderboardEntries: LeaderboardEntry[] = []
    for (const entry of entries)
    {
        const user = userIdToUsers.get(entry.user_id)
        if(!user) continue
        leaderboardEntries.push({...entry, position: 0, user: user})
    }
    setLeaderboardEntryPositions(leaderboardEntries)
    return leaderboardEntries
}

export function setLeaderboardEntryPositions(entries: LeaderboardEntry[])
{
    //Positions looks like [[entry1, entry2], [entry3], [entry4]], index+1 is place in leaderboard
    const positions = [];
    //compute place in positions array
    for (const entry of entries) 
    {
        // add first entry
        if (positions.length === 0) 
        {
            positions.push([entry]);
            continue;
        }
        let i = 0;
        let inserted = false;
        while (i < positions.length && !inserted) 
        {
            if (entry.time < positions[i][0].time) 
            {
                //insert [entry] at i
                positions.splice(i, 0, [entry]);
                inserted = true;
            }        
            else if (entry.time === positions[i][0].time) {
                //append entry to positions[i]
                positions[i].push(entry);
                inserted = true;
            } 
            else 
            {
                i++;
            }
        }
        if (!inserted) 
        {
            positions.push([entry]);
        }
    }
    //set position of each entry object
    for (let j = 0; j < positions.length; j++) 
    {
        for (const entry of positions[j]) 
        {
            entry.position = j + 1;
        }
    }
}