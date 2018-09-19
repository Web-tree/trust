import {TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ConfigService} from "./config.service";
import {AuthenticationService} from "./authentication.service";
import {TokenService} from "./token.service";


describe('AuthenticationService', () => {

  let authService: AuthenticationService;
  let httpMock: HttpTestingController;
  let configService: ConfigService;
  let tokenService: TokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TokenService, ConfigService, AuthenticationService],
      imports: [HttpClientTestingModule]
    });
    httpMock = TestBed.get(HttpTestingController);
    authService = TestBed.get(AuthenticationService);
    configService = TestBed.get(ConfigService);
    tokenService = TestBed.get(TokenService);
  });

  it('#login should sent request with provider and token', () => {
    const provider: string = 'unrealProvider';
    const token: string = 'randomToken';
    const testRequestBody: any = {provider: provider, token: token};
    authService.socialLogin(provider, token).subscribe(()=>{});
    const req = httpMock.expectOne(configService.getBackUrl() + '/rest/social/login');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(testRequestBody);
    httpMock.verify();
  });

  it('#logout should remove token from local storage', () => {
    const tokenSpy = jasmine.createSpyObj('TokenService', ['removeToken']);
    let authServiceWithSpy = new AuthenticationService(null, tokenSpy, null);
    authServiceWithSpy.logout();
    expect(tokenSpy.removeToken).toHaveBeenCalled();
    });

  it('#isAuthorized should return true if token exists', () => {
    const tokenSpy = jasmine.createSpyObj('TokenService', ['tokenExists']);
    tokenSpy.tokenExists.and.returnValue(true);
    let authServiceWithSpy = new AuthenticationService(null, tokenSpy, null);
    expect(authServiceWithSpy.isAuthorized()).toBeTruthy();
    });

  it('#isAuthorized should return false if token does not exist', () => {
    const tokenSpy = jasmine.createSpyObj('TokenService', ['tokenExists']);
    tokenSpy.tokenExists.and.returnValue(false);
    let authServiceWithSpy = new AuthenticationService(null, tokenSpy, null);
    expect(authServiceWithSpy.isAuthorized()).toBeFalsy();
    });
});

