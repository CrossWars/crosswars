from dataclasses import dataclass
from typing import Mapping, NewType, Optional

UserId = NewType('UserId', int)
Time = NewType('Time', int)


@dataclass
class LeaderboardRecord:
        userId: UserId
        time: Time
        rank: Optional[int]


# returns records of [userId, time, position]
class Leaderboard:
    def __init__(self, time_map: Mapping[UserId, Time], name_map: Mapping[UserId, str]):
        self.name_map = name_map
        self.records = sorted([LeaderboardRecord(uId, time, None) for uId, time in time_map.items()],
                              key=lambda x: x.time)
        rank = 1
        self.records[0].rank = rank
        for i in range(1, len(self.records)):
            if self.records[i].time > self.records[i - 1].time:
                rank += 1
            self.records[i].rank = rank

    def __str__(self):
        # TODO: add streak ticker, pass in previous streak data to leaderboard init
        msg = "Today's Leaderboard: "
        for record in self.records:
            secs = record.time % 60
            mins = int(record.time / 60)
            msg += f'\n<b>{record.rank}</b> {self.name_map[record.userId]} - {mins}:{"0" if secs < 10 else ""}{secs} '
        return msg

