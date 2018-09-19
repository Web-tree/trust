import {TestBed, async} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {RegisterComponent} from "./register/register.component";
import {AlertComponent} from "./alert/alert.component";
import {MenuComponent} from "./menu/menu.component";
import {HttpClientModule} from "@angular/common/http";
import {AppRoutingModule, routes} from "./app-routing.module";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {RouterTestingModule} from "@angular/router/testing";
import {AlertService} from "./_services/alert.service";
import {UserService} from "./_services/user.service";
import {Subject} from "rxjs/internal/Subject";
import {LoginComponent} from "./login/login.component";
import {TokenService} from "./_services/token.service";
import {SocialLoginComponent} from "./social-login/social-login.component";

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[
        BrowserModule,
        FormsModule,
        AppRoutingModule,
        HttpClientModule,
        RouterTestingModule
      ],
      declarations: [
        AppComponent,
        RegisterComponent,
        AlertComponent,
        MenuComponent,
        LoginComponent,
        SocialLoginComponent
      ],
      providers:[
        AlertService,
        UserService,
        Subject,
        TokenService
      ]
    }).compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
});
