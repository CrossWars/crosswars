//Source: https://stackoverflow.com/questions/470690/how-to-automatically-generate-n-distinct-colors
//If case arises where more than 22 distinct colors are needed, look into a programmatic approach

const distinct_colors: string[] = [
  '#3da8ff', //light blue
  '#eb384d', //vivid red
  '0xFFB300',

  '0x93AA00', //yellowish green
  '0xFF7A5C', //yellowish pink
  '0x817066', //medium grey
  '0xCEA262',
  '0x00538A', //strong blue

  '0xFF8E00',
  '0xF6768E',
  '0x53377A', //strong violet
  '0x007D34',
  '0x803E75',

  '0xFF6800',
  '0xB32851',
  
  
  '0xF4C800',
  '0x7F180D',

  '0x593315',
  '0xF13A13',
  '0x232C16',
];

//consistent_end useful when needing same color at end - e.g. when displaying ties on heatmap
export function getNDistinctColors(n: number, consistent_end = false): string[] {
  const colors = [];
  if (consistent_end) {
    for (let i = 0; i < n - 1; i += 1) {
      colors.push(distinct_colors[i % distinct_colors.length]);
    }
    colors.push(distinct_colors[distinct_colors.length - 1]); 
  }
  else {
    for (let i = 0; i < n; i += 1) {
        colors.push(distinct_colors[i % distinct_colors.length]);
      }
  }
  return colors
}
