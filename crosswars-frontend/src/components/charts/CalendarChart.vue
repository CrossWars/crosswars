<template lang="">
    <div>
        <q-expansion-item icon="calendar_today" :label="label">
            <v-chart :option="heatmap" class="chart" autoresize/>
        </q-expansion-item>
  </div>
</template>
<script lang='ts'>
import { Entry } from 'src/models/Entries/entries';
import { defineComponent, PropType } from 'vue';
import { formatTime} from 'src/utilities/time'

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
        entries:  
        {
            type: [] as PropType<Entry[]>,
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
  methods: {
      getData(entries: Entry[]) {
          let data = entries.map((e) => [e.date, e.time])
          console.log(data);
          return data;
      }
  },
  computed: {
    other: function() {
        return 0
    },
    heatmap: function() {
        let options: ECBasicOption = {
            tooltip: {
                position: 'top',
                formatter: function (p: any) {
                    const fDate = time.format('yyyy/MM/dd', p.data[0], false)
                    const fTime = formatTime(p.data[1])
                    return '(' + fDate + ') ' + fTime
                }
            },
            visualMap: {
                maxOpen: true,
                calculable: true,
                min: 5,
                orient: 'horizontal',
                align: 'auto',
                left: 'center',
                top: 0,
                itemHeight: 250,
                formatter: function (v: any) {
                    return formatTime(Math.floor(v))
                }
            },
            calendar: [{
                top: 100,
                left: 'center',
                range: (new Date()).getFullYear(),
                borderWidth: 0.5,
                orient: 'vertical'
                }
            ],
            series: [{
                type: 'heatmap',
                coordinateSystem: 'calendar',
                data: this.entries.map((e) => [e.date, e.time]),
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
  }
})
</script>
<style scoped>
    .chart {
        height: 1200px;
    }
</style>