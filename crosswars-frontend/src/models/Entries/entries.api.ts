import { api } from 'src/boot/axios';
import { Entry, PostEntry } from './entries';

function createEntryFromData(entryData: any): Entry
{
    return {date: entryData.date, time: entryData.time, user_id: entryData.user_id}
}
export async function getEntriesByUserId(user_id: string, from_date?: string, to_date?: string): Promise<Entry[]>
{
    let from_param = ''
    let to_param = ''
    if (from_date !== undefined)
    {
        from_param = `&from_date=${from_date}`
        if (to_date !== undefined)
        {
            to_param = `&to_date=${to_date}`
        }
    }
    return api.get(`/entries/users?user_id=${user_id}${from_param}${to_param}`)
    .then((entriesResponse) => {
        const entries: Entry[] = []
        for (const entryData of entriesResponse.data)
        {
            entries.push(createEntryFromData(entryData))
        }
        return entries
    })
}

export async function getEntriesByGroupId(group_id: string, from_date?: string, to_date?: string): Promise<Entry[]>
{
    let from_param = ''
    let to_param = ''
    if (typeof from_date !== undefined)
    {
        from_param = `&from_date=${from_date}`
        if (typeof to_date !== undefined)
        {
            to_param = `&to_date=${to_date}`
        }
    }
    return api.get(`/entries/groups?group_id=${group_id}`)
    .then((entriesResponse) => {
        const entries:Entry[] = []
        for (const entryData of entriesResponse.data)
        {
            entries.push(createEntryFromData(entryData))
        }
        return entries;
    })
}
export async function postEntry(entry: PostEntry){
    return api.post('/entries', entry)
}