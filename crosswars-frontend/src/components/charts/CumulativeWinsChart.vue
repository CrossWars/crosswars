<template>
    <apexchart v-if="entries.length > 1" type="bar" height="200" :options="chartOptions" :series="series"></apexchart>
</template>;

<script lang="ts">
import { LeaderboardEntry } from 'src/models/Entries/entries';
import { defineComponent, PropType } from 'vue';
import VueApexCharts from 'vue3-apexcharts';

export default defineComponent({
  name: 'CumulativeWinsChart',
  props: {
    entries:  
    {
      type: Array as PropType<LeaderboardEntry[]>,
      default: () => []
    },
  },
  components: {
    apexchart: VueApexCharts,
  },
  computed: {
    categories: function(): string[] {
      return this.entries.map((e)=>e.user.name)
    },
    series: function() {
      const data: number[] = this.entries.map((e)=>e.time)
      return [{
        name: 'Todays Times',
        data: data
      }]
    }
  },
  watch: {
    categories() {
      this.chartOptions.xaxis.categories = this.categories
    }
  },
  data () {
    return {
      chartOptions: {
        colors: ['#5fbae8', '#17ead9', '#f02fc2'],
        animations: {
          enabled: true,
          easing: 'easeinout',
          speed: 1000
        },
        grid: {
          show: true,
          strokeDashArray: 0,
          yaxis: {
            lines: {
              show: true
            }
          }
        },
        title: {
          text: 'Column',
          align: 'left',
          style: {
            color: '#FFF'
          }
        },
        fill: {
          type: 'gradient',
          gradient: {
            shade: 'dark',
            type: 'vertical',
            shadeIntensity: 0.5,
            inverseColors: false,
            opacityFrom: 1,
            opacityTo: 0.8,
            stops: [0, 100]
          }
        },
        dataLabels: {
          enabled: true
        },
        stroke: {
          width: 0
        },
        xaxis: {
          type: 'category',
          categories: this.entries.map((e)=>e.user.name),
          labels: {
            show: true,
            style: {
              fontsize: '12px'
            }
          }
        },
        yaxis: {
          title: {
            text: '$ (thousands)',
            style: {
              color: '#FFF'
            }
          },
          labels: {
            style: {
              colors: '#fff'
            }
          }
        },
        tooltip: {
          y: {
            formatter: function (timeInSeconds: number) {
              const mins = Math.floor(timeInSeconds / 60);
              const secs = timeInSeconds % 60;
              return `${mins}:${secs < 10 ? '0' : ''}${secs}`
            }
          }
        }
      }
    }
    }
});
</script>
<style scoped>
</style>
