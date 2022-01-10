import jwt_decode, {JwtPayload} from 'jwt-decode';

export function isJWTExpired(token: string) {
    const decoded = jwt_decode<JwtPayload>(token)
    if(decoded.exp !== undefined)
    {
        return Date.now() >= decoded.exp * 1000
    }
    return true
}

