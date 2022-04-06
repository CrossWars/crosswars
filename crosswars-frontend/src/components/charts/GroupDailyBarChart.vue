<template>
  <apexchart
    v-if="entries.length > 1"
    type="bar"
    height="200"
    :options="chartOptions"
    :series="series"
  ></apexchart>
</template>
;

<script lang="ts">
import { LeaderboardEntry } from 'src/models/Entries/entries';
import { formatTime } from 'src/utilities/time';
import { defineComponent, PropType } from 'vue';
import VueApexCharts from 'vue3-apexcharts';

export default defineComponent({
  name: 'GroupDailyBarChart',
  props: {
    entries: {
      type: Array as PropType<LeaderboardEntry[]>,
      default: () => [],
    },
  },
  components: {
    apexchart: VueApexCharts,
  },
  computed: {
    categories: function (): string[] {
      return this.entries.map((e) => e.user.name);
    },
    series: function () {
      const data: number[] = this.entries.map((e) => e.time);
      return [
        {
          name: 'Todays Times',
          data: data,
        },
      ];
    },
  },
  watch: {
    categories() {
      this.chartOptions.xaxis.categories = this.categories;
    },
  },
  data() {
    return {
      chartOptions: {
        colors: ['#3da8ff'],
        animations: {
          enabled: true,
          easing: 'easeinout',
          speed: 1000,
        },
        grid: {
          show: false,
          strokeDashArray: 0,
          yaxis: {
            lines: {
              show: true,
            },
          },
        },
        title: {
          text: 'Column',
          align: 'left',
          style: {
            color: '#FFF',
          },
        },
        dataLabels: {
          enabled: true,
          formatter: function (value: number) {
            return formatTime(value);
          },
        },
        stroke: {
          width: 0,
        },
        xaxis: {
          type: 'category',
          categories: this.entries.map((e) => e.user.name),
          labels: {
            show: true,
            style: {
              fontsize: '12px',
            },
          },
        },
        yaxis: {
          title: {
            text: '$ (thousands)',
            style: {
              color: '#FFF',
            },
          },
          labels: {
            style: {
              colors: '#fff',
            },
          },
        },
        tooltip: {
          y: {
            formatter: function (timeInSeconds: number) {
              const mins = Math.floor(timeInSeconds / 60);
              const secs = timeInSeconds % 60;
              return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
            },
          },
        },
        chart: {
          toolbar: {
            show: false,
          },
          offsetX: -25
        },
      },
    };
  },
});
</script>
<style scoped></style>
