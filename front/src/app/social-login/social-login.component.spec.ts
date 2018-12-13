import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DebugElement} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientModule, HttpErrorResponse} from '@angular/common/http';

import {Subject} from 'rxjs/internal/Subject';
import {By} from '@angular/platform-browser';

import {AuthenticationService} from '../_services/authentication.service';
import {TokenService} from '../_services/token.service';
import {AlertService} from '../_services/alert.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfigService} from '../_services/config.service';
import {AuthService, SocialUser} from 'angularx-social-login';

import {of} from 'rxjs/internal/observable/of';
import {throwError} from 'rxjs/internal/observable/throwError';
import {SocialLoginComponent} from './social-login.component';
import {promise} from "selenium-webdriver";


describe('SocialLoginComponent', () => {
  let component: SocialLoginComponent;
  let loginFixture: ComponentFixture<SocialLoginComponent>;
  let signInButton: DebugElement;
  let authenticationService: AuthenticationService;
  let tokenService: TokenService;
  let router: Router;
  let alertService: AlertService;
  const authService = jasmine.createSpyObj('AuthService', ['signIn']);
  const token = 'randomToken';

  const provider = 'unrealProvider';
  const userData: SocialUser = {
    provider: 'randomWord',
    id: '1234567654321',
    email: 'test@webtree.org',
    name: 'someUsername',
    photoUrl: 'referenceToAvatar',
    firstName: 'Myname',
    lastName: 'Mysurname',
    authToken: 'a6s6d7f8g9h0j',
    idToken: 'weqwecqwcqwecwce'
  };

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
      declarations: [SocialLoginComponent]

    })
      .compileComponents();
  }));

  beforeEach(() => {
    loginFixture = TestBed.createComponent(SocialLoginComponent);
    authenticationService = TestBed.get(AuthenticationService);
    router = TestBed.get(Router);
    tokenService = TestBed.get(TokenService);
    alertService = TestBed.get(AlertService);
    component = loginFixture.componentInstance;
    authService.signIn.calls.reset();
    signInButton = loginFixture.debugElement.query(By.css('button'));
    authService.signIn.and.returnValue(Promise.resolve(userData));
    loginFixture.detectChanges();
  });

  it('#should create component', () => {
    expect(component).toBeTruthy();
  });

  it('#should handle event when push sign in with* button', () => {
    spyOn(component, 'socialSignIn').and.callThrough();
    signInButton.triggerEventHandler('click', null);
    expect(component.socialSignIn).toHaveBeenCalled();
  });

  it('#should make call to social provider api', async(() => {
    component.socialSignIn(provider);
    loginFixture.whenStable().then(() => {
      loginFixture.detectChanges();
      expect(authService.signIn).toHaveBeenCalledWith(provider);
    });
  }));

  it('#should call backend api', async(() => {
    spyOn(authenticationService, 'socialLogin').and.returnValue(of(token));
    component.socialSignIn(provider);
    loginFixture.whenStable().then(() => {
      expect(authenticationService.socialLogin).toHaveBeenCalledWith(userData.provider, userData.authToken);
    });
  }));

  it('#should get token form backend and save it', async(() => {
    spyOn(authenticationService, 'socialLogin').and.returnValue(of(token));
    spyOn(tokenService, 'saveToken');
    component.socialSignIn(provider);
    loginFixture.whenStable().then(() => {
      expect(tokenService.saveToken).toHaveBeenCalledWith(token);
    });
  }));

  it('#should navigate to url when get data from backend', async(() => {
    spyOn(authenticationService, 'socialLogin').and.returnValue(of(token));
    spyOn(router, 'navigate');
    component.socialSignIn(provider);
    loginFixture.whenStable().then(() => {
      expect(router.navigate).toHaveBeenCalled();
    });
  }));

  it('#should handle error when backend response with error', async(() => {
    const errorMsg = 'Some error';
    spyOn(alertService, 'error');
    spyOn(authenticationService, 'socialLogin').and.returnValue(throwError(new HttpErrorResponse({error: errorMsg})));
    component.socialSignIn(provider);
    loginFixture.whenStable().then(() => {
      expect(alertService.error).toHaveBeenCalledWith(errorMsg);
    })
  }));
});
