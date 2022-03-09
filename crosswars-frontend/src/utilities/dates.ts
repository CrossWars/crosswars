export function monthDiff(dateFrom: Date, dateTo: Date) {
    return dateTo.getMonth() - dateFrom.getMonth() + 
    (12 * (dateTo.getFullYear() - dateFrom.getFullYear()))
}
export function dayDiff(dateFrom: Date, dateTo: Date) {
    const diffTime = Math.abs(dateFrom.getTime() - dateTo.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
}
export function parseDate(dateStr: string) {
    const dateSplit = dateStr.split('-')
    return new Date(parseInt(dateSplit[0]), parseInt(dateSplit[1]) - 1, parseInt(dateSplit[2]))
}