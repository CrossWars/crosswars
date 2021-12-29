export function formatTime(timeInSeconds: number) : string{
    const mins = Math.floor(timeInSeconds / 60);
    const secs = timeInSeconds % 60;
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}