<template lang="">
  <div :v-if="data.length > 0">
    <q-expansion-item icon="calendar_today" :label="label">
      <v-chart :option="heatmap" class="chart" autoresize />
    </q-expansion-item>
  </div>
</template>
<script lang="ts">
import { Win } from 'src/models/Wins/wins';
import { defineComponent, PropType } from 'vue';

import { use, time} from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { HeatmapChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  VisualMapComponent,
  GridComponent,
  CalendarComponent,
} from 'echarts/components';
import VChart, { THEME_KEY } from 'vue-echarts';
import { ECBasicOption } from 'echarts/types/dist/shared';
import { User } from 'src/models/Users/users';

use([
  CanvasRenderer,
  HeatmapChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  VisualMapComponent,
  GridComponent,
  CalendarComponent
]);

export default defineComponent({
    name: 'CalendarChart',
    props: {
        wins:
        {
            type: [] as PropType<Win[]>,
            default: () => []
        },
        users:
        {
        type: Array as PropType<User[]>,
        default: () => []
        },
  },
  components: {
    VChart
  },
  provide: {
      [THEME_KEY]: 'light'
  },
  data() {
    return {
      label: `${(new Date()).getFullYear()} Heatmap`
    };
  },
  computed: {
    minDate: function(): string {
        return this.wins.map((w) => w.date).sort()[0]
    },
    maxDate: function(): string {
        const dates = this.wins.map((w) => w.date).sort()
        return dates[dates.length - 1]
    },
    //returns map of Date -> Array of IDs for days with ties
    ties: function(): Map<string, string[]> {
        const ties = new Map<string, string[]>()
        for(const win of this.wins)
        {
            if(ties.has(win.date)) {
                ties.get(win.date)!.push(win.user_id)
            }
            else {
                ties.set(win.date, [win.user_id])
            }
        }
        const tieEntries = ties.entries();
        for(const tieEntry of tieEntries)
        {
            if(tieEntry[1].length == 1) {
                ties.delete(tieEntry[0])
            }
        }
        return ties
    },
    categories: function(): string[] {
        const categories = [...new Set(this.wins.map((w) => w.user_id))]
        if(this.ties.size > 0) {
           categories.push('0')
        }
        return categories
    },
    data: function(): string[][] {
        var data = this.wins.reduce((result, w) => {
            if(!this.ties.has(w.date)) {
                result.push([w.date, w.user_id])
            }
            return result
        }, [] as string[][]);
        for(const date of this.ties.keys())
        {
            data.push([date, '0'])
        }
        return data
    },
    usersMap: function(): Map<string, User> {
      return this.users.reduce( function(map, user){
        map.set(user.id, user);
        return map;
      }, new Map<string, User>());
    },
    heatmap: function() {
        let options: ECBasicOption = {
            tooltip: {
                position: 'top',
                formatter: (p: any) => {
                    const date: string = p.data[0]
                    const fDate = time.format('yyyy/MM/dd', date, false)
                    var formatString = '(' + fDate + ') '
                    if(p.data[1] == '0') {
                        formatString += this.ties.get(date)!.map((id) => this.usersMap.get(id)!.name).join(', ')
                    }
                    else {
                        formatString += this.usersMap.get(p.data[1])?.name
                    }
                    return formatString
                }
            },
            visualMap: {
                orient: 'horizontal',
                type: 'piecewise',
                categories: this.categories,
                align: 'auto',
                left: 'center',
                top: 0,
                inRange: {
                    color: ['red', 'orange', 'green', 'blue', 'purple'],
                },
                formatter: (id: string) => {
                    if(id=='0') return 'Tie'
                    return this.usersMap.get(id)?.name
                }
            },
            calendar: [{
                top: 100,
                left: 'center',
                range: [this.minDate, this.maxDate],
                borderWidth: 0.5,
                orient: 'vertical'
                }
            ],
            series: [{
                type: 'heatmap',
                coordinateSystem: 'calendar',
                data: this.data,
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }]
        }
        return options
    }
  },
})
</script>
<style scoped>
.chart {
  height: 1200px;
}
</style>
