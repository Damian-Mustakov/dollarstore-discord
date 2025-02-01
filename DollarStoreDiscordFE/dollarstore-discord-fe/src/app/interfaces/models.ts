export interface Channel {
    id: number;
    name: string;
    owner: DiscordUser;
    isDeleted: boolean;
    isFriendChat: boolean;
  }
  
  export interface ChannelMember {
    id: number;
    channel: Channel;
    role: RoleType;
    user: DiscordUser;
  }
  
  export interface DiscordUser {
    id: number;
    username: string;
    email: string;
    isDeleted: boolean;
  }
  
  export interface Message {
    id: number;
    textMessage: string;
    timestamp: Date;
    sender: DiscordUser;
    channel: Channel;
  }
  
  export enum RoleType {
    OWNER = 'OWNER',
    ADMIN = 'ADMIN',
    GUEST = 'GUEST'
  }