import { api } from 'src/boot/axios';
import { Win, WinCount } from './wins';

function createWinCountFromData(winCountData: any): WinCount {
  return {
    user_id: winCountData.user_id,
    group_id: winCountData.group_id,
    wins: winCountData.wins,
    from_date: winCountData.from_date,
    to_date: winCountData.to_date
  };
}
function createWinFromData(winData: any): Win {
    return {
      user_id: winData.user_id,
      group_id: winData.group_id,
      date: winData.date
    };
  }
export async function getWinCountsByGroupId(
  group_id: string
): Promise<WinCount[]> {
  return api
    .get(`/wins/counts/groups?group_id=${group_id}`)
    .then((winCountsResponse) => {
      const winCounts: WinCount[] = [];
      for (const winCountData of winCountsResponse.data) {
        winCounts.push(createWinCountFromData(winCountData));
      }
      return winCounts;
    });
}
export async function getWinsByGroupId(
    group_id: string
  ): Promise<Win[]> {
    return api
      .get(`/wins/groups?group_id=${group_id}`)
      .then((winsResponse) => {
        const wins: Win[] = [];
        for (const winData of winsResponse.data) {
          wins.push(createWinFromData(winData));
        }
        return wins;
      });
}