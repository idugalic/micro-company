import { NgModule } from '@angular/core';
import { RouterModule, Routes} from '@angular/router';

// Main router - redirect to 'blog' url (module) by default
const appRoutes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'blog',
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes),
  ],
  exports: [
    RouterModule,
  ],
  providers: [],
})

export class AppRoutingModule { }
