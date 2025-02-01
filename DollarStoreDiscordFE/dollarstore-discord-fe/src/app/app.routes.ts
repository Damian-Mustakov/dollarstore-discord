import { Routes } from "@angular/router";
import { LoginComponent } from "./components/auth/login/login.component";
import { RegisterComponent } from "./components/auth/register/register.component";
import { ChannelSettingsComponent } from "./components/channel-settings/channel-settings.component";
import { WelcomeScreenComponent } from "./components/welcome-screen/welcome-screen.component";
import { authGuard } from "./guards/auth.guard";
import { channelSettingsGuard } from "./guards/channel-settings.guard";
import { AuthLayoutComponent } from "./layouts/auth-layout/auth-layout.component";
import { MainLayoutComponent } from "./layouts/main-layout/main-layout.component";
import { ChatComponent } from "./components/chat/chat.component";

export const routes: Routes = [
    {
      path: '',
      component: AuthLayoutComponent,
      children: [
        { path: 'login', component: LoginComponent },
        { path: 'register', component: RegisterComponent },
      ],
    },
    {
      path: '',
      component: MainLayoutComponent,
      canActivate: [authGuard], 
      children: [
        { path: 'channels', component: WelcomeScreenComponent },
        { path: 'channels/:id', component: ChatComponent },
        {
          path: 'channels/:id/settings',
          component: ChannelSettingsComponent,
          canActivate: [channelSettingsGuard], 
        },
        { path: 'friends', component: ChatComponent },
      ],
    },
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' },
  ];