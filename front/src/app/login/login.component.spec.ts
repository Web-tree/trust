import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {DebugElement} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientModule} from '@angular/common/http';

import {Subject} from 'rxjs/internal/Subject';
import {By} from '@angular/platform-browser';

import {AuthenticationService} from '../_services/authentication.service';
import {TokenService} from '../_services/token.service';
import {AlertService} from '../_services/alert.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfigService} from '../_services/config.service';
import {AuthService} from 'angularx-social-login';
import {SocialLoginComponent} from '../social-login/social-login.component';


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let signInButton: DebugElement;
  let authenticationService: AuthenticationService;
  let tokenService: TokenService;
  let router: Router;
  let alertService: AlertService;
  const authService = jasmine.createSpyObj('AuthService', ['signIn']);


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, RouterTestingModule, HttpClientModule],
      providers: [
        {provide: AuthService, useValue: authService},
        {provide: ActivatedRoute, useValue: {snapshot: {queryParams: '/'}}},
        Subject,
        TokenService,
        AlertService,
        ConfigService,
        AuthenticationService,
      ],
      declarations: [LoginComponent, SocialLoginComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    authenticationService = TestBed.get(AuthenticationService);
    router = TestBed.get(Router);
    tokenService = TestBed.get(TokenService);
    alertService = TestBed.get(AlertService);
    component = fixture.componentInstance;
    authService.signIn.calls.reset();
    signInButton = fixture.debugElement.query(By.css('button'));
    fixture.detectChanges();
  });

  it('#should create component', () => {
    expect(component).toBeTruthy();
  });
});
