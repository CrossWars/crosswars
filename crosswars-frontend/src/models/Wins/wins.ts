export interface Win {
    user_id: string;
    group_id: string;
    date: string;
}

export interface WinCount {
    user_id: string;
    group_id: string;
    wins: number;
    from_date?: string;
    to_date?: string;
}

export interface LeaderboardWinCount extends WinCount {
    position: number;
}