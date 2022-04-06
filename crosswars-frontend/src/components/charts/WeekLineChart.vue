<template>
  <div>
    <q-expansion-item icon="show_chart" label="Line Graph" default-opened>
      <div class="q-px-md">
        <q-btn-toggle
          v-model="selection"
          spread
          toggle-color="primary"
          text-color="primary"
          :options="button_options"
        />
      </div>
      <apexchart
        ref="chart"
        v-if="entries.length > 1"
        type="line"
        height="300"
        :options="chartOptions"
        :series="series"
      ></apexchart>
    </q-expansion-item>
  </div>
</template>
;

<script lang="ts">
import { Entry } from 'src/models/Entries/entries';
//import { formatTime } from 'src/utilities/time';
import { defineComponent, PropType } from 'vue';
import VueApexCharts from 'vue3-apexcharts';

import { parseDate } from 'src/utilities/dates';
import { formatTime } from 'src/utilities/time';

export default defineComponent({
  name: 'WeekLineChart',
  props: {
    entries: {
      type: Array as PropType<Entry[]>,
      default: () => [],
    },
    min_date: {} as PropType<string>,
    max_date: {} as PropType<string>,
    day_diff: {} as PropType<Number>,
    avg_time: {} as PropType<Number>,
    min_time_entry: Object as PropType<Entry>,
  },
  components: {
    apexchart: VueApexCharts,
  },
  computed: {
    series: function () {
      const data: (Date | number)[][] = this.entries.map((e) => [
        parseDate(e.date),
        e.time,
      ]);
      return [
        {
          name: 'Submitted Times',
          data: data,
        },
      ];
    },
    button_options: function () {
      const opts = [];
      if (this.day_diff! >= 7) {
        opts.push({ label: '1W', value: 'one_week' });
      }
      if (this.day_diff! >= 30) {
        opts.push({ label: '1M', value: 'one_month' });
      }
      if (this.day_diff! >= 365) {
        opts.push({ label: '1Y', value: 'one_year' });
      }
      opts.push({ label: 'ALL', value: 'all' });
      return opts;
    },
    
  },
  async mounted() {
    await new Promise(r => setTimeout(r, 1));
    this.updateData('one_week')
  },
  methods: {
    last_week_or_min: function(): Date {
      if(this.day_diff! >= 7)
      {
        const last_date = parseDate(this.max_date!);
        const last_week = new Date(
          last_date.getFullYear(),
          last_date.getMonth(),
          last_date.getDate() - 7
        );
        return last_week;
      }
      return parseDate(this.min_date!);
    },
    range_from_last_week_or_min: function() {
      const fromDate = this.last_week_or_min();
      const toDate = parseDate(this.max_date!);
      let min_time: number | undefined = undefined;
      let max_time: number | undefined = undefined;
      this.entries.forEach((e) => {
        const ed = parseDate(e.date);
        if(fromDate.getTime() <= ed.getTime() && ed.getTime() <= toDate.getTime()) {
          if(min_time === undefined || e.time < min_time) {
            min_time = e.time;
          }
          if(max_time === undefined || e.time > max_time) {
            max_time = e.time;
          }
        }
      })
      return [min_time, max_time]
    },
    updateData: function (timeline: string) {
      this.selection = timeline;
      const last_date = parseDate(this.max_date!);
      const last_week = new Date(
        last_date.getFullYear(),
        last_date.getMonth(),
        last_date.getDate() - 7
      );
      const last_month = new Date(
        last_date.getFullYear(),
        last_date.getMonth() - 1,
        last_date.getDate()
      );
      const last_year = new Date(
        last_date.getFullYear() - 1,
        last_date.getMonth(),
        last_date.getDate()
      );
      switch (timeline) {
        case 'one_week':
          // @ts-ignore
          this.$refs.chart.zoomX(last_week.getTime(), last_date.getTime());
          break;
        case 'one_month':
          // @ts-ignore
          this.$refs.chart.zoomX(last_month.getTime(), last_date.getTime());
          break;
        case 'one_year':
          // @ts-ignore
          this.$refs.chart.zoomX(last_year.getTime(), last_date.getTime());
          break;
        case 'all':
          // @ts-ignore
          this.$refs.chart.zoomX(
            parseDate(this.min_date!).getTime(),
            parseDate(this.max_date!).getTime()
          );
          break;
      }
    },
  },
  data() {
    return {
      selection: 'one_week',
      chartOptions: {
        series: [],
        chart: {
          id: 'linechart',
          height: 350,
          type: 'line',
          toolbar: {
            show: false,
          },
          zoom: {
            autoScaleYaxis: true,
          },
        },

        stroke: {
          width: 5,
          curve: 'smooth',
        },
        xaxis: {
          type: 'datetime',
          //min: this.last_week_or_min().getTime(),
          //max: parseDate(this.max_date!).getTime(),
        },
        yaxis: {
          forceNiceScale: true,
          labels: {
            formatter: function (val: number) {
              return formatTime(val);
            },
          },
        },
        markers: {
          size: 4,
        },
        annotations: {
          yaxis: [
            {
              y: this.avg_time,
              strokeDashArray: 7,
              borderColor: '#f74f7c',
              label: {
                position: 'right',
                show: true,
                text: 'Avg',
                style: {
                  color: '#fff',
                  background: '#f74f7c',
                },
              },
            },
          ],
          points: [
            {
              x: parseDate(this.min_time_entry!.date).getTime(),
              y: this.min_time_entry!.time,
              marker: {
                size: 4,
                fillColor: '#f74f7c'
              },
              borderColor: '#f74f7c',

              label: {
                borderColor: '#f74f7c',
                text: 'Best',
                offsetY: 35,
                offsetX: -20,
                style: {
                  color: '#fff',
                  background: '#f74f7c',
                },
              },
            },
          ],
        },
      },
    };
  },
  watch: {
    selection() {
      this.updateData(this.selection);
    },
    day_diff() {},
  },
  
});
</script>
<style scoped></style>
