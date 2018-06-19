import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { AlertComponent } from './alert/alert.component';
import { AppRoutingModule } from './/app-routing.module';
import { RegisterComponent } from './register/register.component';
import {FormsModule} from "@angular/forms";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {AlertService} from "./_services/alert.service";
import {Subject} from "rxjs/internal/Subject";

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    AlertComponent,
    RegisterComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [Subject,AlertService],
  bootstrap: [AppComponent]
})
export class AppModule { }
