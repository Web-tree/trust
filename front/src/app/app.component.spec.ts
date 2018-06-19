import {TestBed, async} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {RegisterComponent} from "./register/register.component";
import {AlertComponent} from "./alert/alert.component";
import {MenuComponent} from "./menu/menu.component";
import {routerNgProbeToken} from "@angular/router/src/router_module";
import {AppModule} from "./app.module";
import {HttpClientModule} from "@angular/common/http";
import {AppRoutingModule, routes} from "./app-routing.module";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {RouterTestingModule} from "@angular/router/testing";
import {AlertService} from "./_services/alert.service";
import {UserService} from "./_services/user.service";
import {Subject} from "rxjs/internal/Subject";

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
      ],
      providers:[
        AlertService,
        UserService,
        Subject
      ]
    }).compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
 /* it(`should have as title 'app'`, async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('app');
  }));
  it('should render title in a h1 tag', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to app!');
  }));*/
});
