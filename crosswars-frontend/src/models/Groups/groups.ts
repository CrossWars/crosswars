import { User } from '../Users/users';
export interface Group {
    id: string;
    name: string;
    users?: User[];
}