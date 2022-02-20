import { Group } from '../Groups/groups';
import { User } from '../Users/users';

//simplest entries, for submitting to backend. May exclude date if current crossward day is to be used.
export interface PostEntry {
    date?: string,
    time: number,
    user_id: string
}
//for using an entry when date is required, such as for data visualizations
export interface Entry extends PostEntry {
    date: string;
}
//for displaying entries in a leaderboard, showing position and user info
export interface LeaderboardEntry extends Entry {
    position: number,
    user: User,
}
//for displaying leaderboards among multiple groups, an entry of a given user can belong to multiple groups
export interface CombinedLeaderboardEntry extends LeaderboardEntry {
    groups: Group[]
}