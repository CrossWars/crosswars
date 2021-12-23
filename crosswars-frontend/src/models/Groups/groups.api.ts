import { api } from 'src/boot/axios';
import { Group } from './groups';
import { User } from '../Users/users';

function createGroupFromData(groupData: any, users?: User[]): Group
{
    return {id: groupData.id, name: groupData.name, users: users}
}
export async function getGroupByGroupId(group_id: string): Promise<Group>
{
    return api.get(`/groups/ids?group_id=${group_id}`)
    .then((groupResponse) => {
        return createGroupFromData(groupResponse.data);
    })
}
export async function getGroupsByUserId(user_id: string): Promise<Group[]>
{
    return api.get(`/groups?user_id=${user_id}`)
    .then((groupsResponse) => {
        const groups: Group[] = []
        for (const groupData of groupsResponse.data)
        {
            groups.push(createGroupFromData(groupData))
        }
        return groups
    })
}
export async function getGroupByName(group_name: string): Promise<Group>
{
    return api.get(`/groups/names?group_name=${group_name}`)
    .then((groupResponse) => {
        return createGroupFromData(groupResponse);
    })
}

