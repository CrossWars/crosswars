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
export async function getGroupsByJWT(): Promise<Group[]>
{
    const token = localStorage.getItem('jwt')
    return api.get('/groups',
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    )
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
        return createGroupFromData(groupResponse.data);
    })
}

export async function postNewGroup(group: Group): Promise<Group>
{
    const token = localStorage.getItem('jwt')
    return api.post('/groups/website', {group_id: group.id, name: group.name},
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    ).then((groupResponse) => {
        return createGroupFromData(groupResponse.data)
    })
}

export async function addUserToGroup(group_id: string, user_id: string)
{
    const token = localStorage.getItem('jwt')
    return api.post('/groups/users', null, {
        params: {
            group_id: group_id,
            user_id: user_id
        },
        headers: {
            Authorization: `Bearer ${token}`
        }
    })
}

export async function getIsMember(group_id: string, user_id: string): Promise<boolean>
{
    return api.get('/groups/is_members', {
        params: {
            group_id: group_id,
            user_id: user_id
        }
    }).then((isMemberResponse) => isMemberResponse.data)
}
