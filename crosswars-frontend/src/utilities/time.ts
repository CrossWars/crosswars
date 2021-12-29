export function formatTime(timeInSeconds: number) : string{
    const mins = Math.floor(timeInSeconds / 60);
    let secs = timeInSeconds % 60;
    secs = Math.round(secs * 10) / 10
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}