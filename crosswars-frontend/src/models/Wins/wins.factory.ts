import { WinCount, LeaderboardWinCount } from './wins';
import { getWinCountsByGroupId } from './wins.api';

export async function createLeaderboardWinCounts(group_id: string): Promise<LeaderboardWinCount[]>
{
    const winCount = await getWinCountsByGroupId(group_id)
    return setWinCountPositions(winCount)

}
export function setWinCountPositions(winCounts: WinCount[]): LeaderboardWinCount[]
{
    //Positions looks like [[entry1, entry2], [entry3], [entry4]], index+1 is place in leaderboard
    const positions = [] as WinCount[][];
    //compute place in positions array
    for (const winCount of winCounts) 
    {
        // add first entry
        if (positions.length === 0) 
        {
            positions.push([winCount]);
            continue;
        }
        let i = 0;
        let inserted = false;
        while (i < positions.length && !inserted) 
        {
            if (winCount.wins > positions[i][0].wins) 
            {
                //insert [winCount] at i
                positions.splice(i, 0, [winCount]);
                inserted = true;
            }        
            else if (winCount.wins === positions[i][0].wins) {
                //append winCount to positions[i]
                positions[i].push(winCount);
                inserted = true;
            } 
            else 
            {
                i++;
            }
        }
        if (!inserted) 
        {
            positions.push([winCount]);
        }
    }
    const leaderboardWinCounts = [] as LeaderboardWinCount[];
    //set position of each winCount object
    for (let j = 0; j < positions.length; j++) 
    {
        for (const winCount of positions[j]) 
        {
            leaderboardWinCounts.push({...winCount, position: j+1})
        }
    }
    return leaderboardWinCounts
}