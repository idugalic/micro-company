import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { BlogPostsModule } from './blog-posts/blog-posts.module';
import { ProjectsModule } from './projects/projects.module';
import { AppRoutingModule } from './app-routing.module';
import { MaterialModule } from '@angular/material';
import { EventManager } from './shared/event-manager.service';
import { DatePickerModule } from 'ng2-datepicker';
import { customHttpProvider } from './shared/interceptor/http.provider';
import { alertServiceProvider } from './shared/alert/alert.provider';
import { AlertComponent } from './shared/alert/alert.component';
import { AlertErrorComponent } from './shared/alert/alert-error.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';



@NgModule({
  declarations: [
    AppComponent, AlertErrorComponent, AlertComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BlogPostsModule,
    ProjectsModule,
    AppRoutingModule,
    DatePickerModule,
    NgbModule.forRoot(),
    MaterialModule.forRoot()
  ],
  providers: [EventManager, customHttpProvider(), alertServiceProvider()],
  bootstrap: [AppComponent]
})
export class AppModule { }
