import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { map } from 'rxjs';
import { ChannelService } from '../services/channel.service';
import { UserService } from '../services/user.service';
import { ChannelMember } from '../interfaces/models';

export const channelSettingsGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const channelService = inject(ChannelService);
  const userService = inject(UserService);
  const router = inject(Router);

  const channelId = Number(route.params['id']);
  const currentUser = userService.getUserFromSession();

  if (!currentUser) {
    router.navigate(['/channels']);
    return false;
  }

  return channelService.getChannelMembers(channelId).pipe(
    map((members: ChannelMember[]) => {
      const userIsAdminOrOwner = members.some(
        (member) =>
          member.user.id === currentUser.id &&
          (member.role === 'OWNER' || member.role === 'ADMIN')
      );

      if (!userIsAdminOrOwner) {
        router.navigate(['/channels']);
      }

      return userIsAdminOrOwner;
    })
  );
};
