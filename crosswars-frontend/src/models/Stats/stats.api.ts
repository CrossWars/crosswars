import { api } from 'src/boot/axios';

export async function getBestTimeByUserId(user_id: string): Promise<number>
{
    return api.get(`/stats/best_times?user_id=${user_id}`)
    .then((timeResponse) => {
        return timeResponse.data;
    })
}
export async function getAverageTimeByUserId(user_id: string): Promise<number>
{
    return api.get(`/stats/averages?user_id=${user_id}`)
    .then((timeResponse) => {
        return timeResponse.data;
    })
}