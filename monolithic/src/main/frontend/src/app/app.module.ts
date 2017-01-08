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

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BlogPostsModule,
    ProjectsModule,
    AppRoutingModule,
    DatePickerModule,
    MaterialModule.forRoot()
  ],
  providers: [EventManager],
  bootstrap: [AppComponent]
})
export class AppModule { }
