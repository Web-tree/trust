import {TestBed} from '@angular/core/testing';
import {ApplicationService} from './application.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ConfigService} from "./config.service";
import {Application} from "../_models/application";
import {ApplicationWrapper} from "../_models/applicaionWrapper";
import {HttpResponse} from "@angular/common/http";

describe('ApplicationService', () => {
  let appService: ApplicationService;
  let httpMock: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApplicationService, ConfigService],
      imports: [HttpClientTestingModule],
    });

    httpMock = TestBed.get(HttpTestingController);
    appService = TestBed.get(ApplicationService);
    configService = TestBed.get(ConfigService);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should return list of applications', () => {
    let appList: Application[] = [{name: 'firstApp', id: 'firstId'}, {name: 'secondApp', id: 'secondId'}];
    spyOn(appService, 'getList').and.callThrough();

    appService.getList().subscribe(list => {
      expect(list).toEqual(appList);
    });
    expect(appService.getList).toHaveBeenCalled();

    const req = httpMock.expectOne(configService.getBackUrl() + '/rest/app/list');
    expect(req.request.method).toEqual('GET');
    req.flush(appList);
  });

  it('should delete application', () => {
    const id: string = 'someId';

    appService.delete(id).subscribe(resp => {
      expect(resp.status).toEqual(204);
    });

    const req = httpMock.expectOne(configService.getBackUrl() + '/rest/app/' + id);
    expect(req.request.method).toEqual('DELETE');
    req.flush(new HttpResponse({status: 204}));
  });

  it('should return new secret', () => {
    const newSecret: String = 'coolSecret';
    const id: String = 'qwerty';

    appService.resetSecret(id).subscribe(secret => {
      expect(secret).toEqual(newSecret);
    });

    const req = httpMock.expectOne(configService.getBackUrl() + '/rest/app/reset/' + id);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(null);
    req.flush(newSecret);
  });

  it('should  perform update name', () => {
    const newName: String = 'zxcvbn';
    const id: String = 'qwerty';

    appService.changeName(id, newName).subscribe(response => {
      expect(response.status).toEqual(200);
    });

    const req = httpMock.expectOne(configService.getBackUrl() + '/rest/app/' + id);
    expect(req.request.method).toEqual('PUT');
    expect(req.request.body).toEqual(newName);
    req.flush(new HttpResponse());
  });

  it('should return new application', () => {
    const name: String = 'zxcvbn';
    const app: ApplicationWrapper = {secret: 'xxcvbn', application: {name: 'asd', id: 'poiu'}};

    appService.add(name).subscribe(newApp => {
      expect(newApp).toEqual(jasmine.objectContaining(app));
    });

    const req = httpMock.expectOne(configService.getBackUrl() + '/rest/app');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(name);
    req.flush(app);
  });
});
