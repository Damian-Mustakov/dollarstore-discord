import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { channelSettingsGuard } from './channel-settings.guard';

describe('channelSettingsGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => channelSettingsGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
