import { api } from 'src/boot/axios';
import { api_key } from 'src/boot/googleApi';
import axios from 'axios'
import { User } from './users';

function createUserFromData(userData: any): User
{
    return {id: userData.user_id, name: userData.name}
}
export async function getUsersByGroupId(group_id: string, photos=false): Promise<User[]>
{
    return api.get(`/groups/users?group_id=${group_id}`)
    .then(async (usersResponse) => {
        const users:User[] = [];
        for (const userData of usersResponse.data)
        {
            const user = createUserFromData(userData);
            if(photos) {
                user.photoUrl = await getGooglePhotoForUser(user.id);
            }
            users.push(user)
        }
        return users;
    })
}

export async function getUserByUserId(user_id: string, photo=false): Promise<User>
{
    const token = localStorage.getItem('jwt')
    return api.get(`/users/ids?user_id=${user_id}`,
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    )
    .then(async (userResponse) => {
        if(userResponse.status == 204) {
            throw new Error('User not found')
        }
        const user = createUserFromData(userResponse.data)
        if(photo) {
            user.photoUrl = await getGooglePhotoForUser(user.id);
        }
        return user;
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

export async function getGooglePhotoForUser(user_id: string): Promise<string | undefined> {
    return axios.get(`https://people.googleapis.com/v1/people/${user_id}?personFields=photos&key=${api_key}`)
    .then((personResponse) => {
        const photos = personResponse.data['photos'][0]
        if(photos['default']) { //returns null if photo is the default pfp
            return undefined;
        }
        return photos['url']
    })
    .catch(function (error) {
        return undefined
    })
}

