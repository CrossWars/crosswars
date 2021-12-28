<template lang="">
    <div>
        <VueECharts :options="heatmap" autoresize/>
    </div>
</template>
<script lang="ts">
import { Entry } from 'src/models/Entries/entries';
import { defineComponent, PropType } from 'vue';


import VueECharts from 'vue-echarts';
import "echarts/lib/chart/heatmap";
import "echarts/lib/component/tooltip";
import "echarts/lib/component/grid";
import "echarts/lib/component/visualMap";
import { ECBasicOption } from 'echarts/types/dist/shared';

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
    VueECharts
  },
  methods: {
      getData(entries: Entry[]) {
          console.log('entries:')
          let data = entries.map((e) => [e.date, e.time])
          console.log(data);
          return data;
      }
  },
  computed: {
    heatmap: function(): ECBasicOption {
        let options: ECBasicOption = {
            tooltip: {
                position: 'top',
            },
            series: [{
                type: 'heatmap',
                coordinateSystem: 'calendar',
                data: [['2020-01-01', 20]],
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
<style lang="">
    
</style>