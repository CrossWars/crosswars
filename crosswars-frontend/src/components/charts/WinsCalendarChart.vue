<template lang="">
  <div class="q-pa-md">
    <q-card>
    <q-expansion-item icon="calendar_today" label="Wins Calendar" default-opened>
      <v-chart :option="heatmap" :style="'height: ' + ((month_diff)*90 +150) +'px'" autoresize/>
    </q-expansion-item>
    </q-card>
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
import { getNDistinctColors} from 'src/utilities/colors';

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
        month_diff: {} as PropType<number>,
  },
  components: {
    VChart
  },
  provide: {
      [THEME_KEY]: 'light'
  },
  data() {
    return {
      label: `${(new Date()).getFullYear()} Wins Calendar`
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
    ties: function(): Map<string, number[]> {
        const ties = new Map<string, number[]>()
        for(const win of this.wins)
        {
            if(ties.has(win.date)) {
                ties.get(win.date)!.push(parseInt(win.user_id))
            }
            else {
                ties.set(win.date, [parseInt(win.user_id)])
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
    colors: function(): string[] {
        const c = getNDistinctColors(this.categories.length, this.ties.size > 0).map(c => c.toLowerCase().replace('0x', '#'));
        return c;
    },
    categories: function(): number[] {
        const categories = [...new Set(this.wins.map((w) => parseInt(w.user_id)))]
        if(this.ties.size > 0) {
           categories.push(0)
        }
        return categories
    },
    data: function(): (string | number)[][] {
        var data = this.wins.reduce((result, w) => {
            if(!this.ties.has(w.date)) {
                result.push([w.date, w.user_id])
            } else {
                result.push([w.date, 0])
            }
            return result
        }, [] as (string | number)[][]);
        return data
    },
    usersMap: function(): Map<number, User> {
      return this.users.reduce( function(map, user){
        map.set(parseInt(user.id), user);
        return map;
      }, new Map<number, User>());
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
                        formatString += this.usersMap.get(parseInt(p.data[1]))?.name
                    }
                    return formatString
                }
            },
            visualMap: {
                orient: 'vertical',
                type: 'piecewise',
                categories: this.categories,
                align: 'auto',
                left: '5%',
                top: '20px',
                inRange: {
                    color: this.colors,
                },
                formatter: (id: number) => {
                    if(id==0) return 'Tie'
                    return this.usersMap.get(id)?.name
                }
            },
            calendar: [{
                top: '80px',
                left: '40%',
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
