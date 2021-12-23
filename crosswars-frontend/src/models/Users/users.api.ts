import { Cookies } from 'quasar';
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

//TODO include auth token
export async function getUserByUserId(user_id: string): Promise<User>
{
    const token = Cookies.get('id_token')
    console.log(`id: ${token}`)
    return api.get(`/users/ids?user_id=${user_id}`,
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    )
    .then((userResponse) => {
        return createUserFromData(userResponse.data)
    })
}

