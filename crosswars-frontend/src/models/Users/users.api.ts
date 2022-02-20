import { api } from 'src/boot/axios';
import { User } from './users';

function createUserFromData(userData: any): User
{
    return {id: userData.user_id, name: userData.name}
}
export async function getUsersByGroupId(group_id: string): Promise<User[]>
{
    return api.get(`/groups/users?group_id=${group_id}`)
    .then((usersResponse) => {
        const users:User[] = [];
        for (const userData of usersResponse.data)
        {
            users.push(createUserFromData(userData))
        }
        return users;
    })
}

export async function getUserByUserId(user_id: string): Promise<User>
{
    const token = localStorage.getItem('jwt')
    return api.get(`/users/ids?user_id=${user_id}`,
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    )
    .then((userResponse) => {
        if(userResponse.status == 204) {
            throw new Error('User not found')
        }
        return createUserFromData(userResponse.data)
    })
}

export async function getUserByJWT(): Promise<User>
{
    const token = localStorage.getItem('jwt')
    return api.get('/users/ids',
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    )
    .then((userResponse) => {
        if(userResponse.status == 204) {
            throw new Error('User not found')
        }
        return createUserFromData(userResponse.data)
    })
}

export async function postNewUser(): Promise<User>
{
    const token = localStorage.getItem('jwt')
    return api.post('/users/website', null,
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    )
}

export async function getGooglePhotoForUser()

