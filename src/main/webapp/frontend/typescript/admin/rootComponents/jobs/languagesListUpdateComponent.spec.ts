import {it, describe, expect, beforeEach, injectAsync, inject, beforeEachProviders} from '@angular/core/testing';
import {TestComponentBuilder} from '@angular/compiler/testing/test_component_builder';
import {ComponentFixture} from '@angular/compiler/testing/test_component_builder';
import {HTTP_PROVIDERS, XHRBackend, Http, ConnectionBackend, RequestMethod} from '@angular/http';
import {Router} from '@angular/router-deprecated';
import {RootRouter} from '@angular/router-deprecated/src/router';
import {MockBackend} from '@angular/http/testing';
import {HttpClient} from '../../util/httpClient';
import {provide} from '@angular/core';
import {HttpClientTestHelper} from '../../util/httpClientTestHelper.spec';
import {LanguagesListUpdateComponent} from './languagesListUpdateComponent';
import {LanguagesListUpdateApi} from './languagesListUpdateApi';
import {COMPILER_PROVIDERS, XHR} from '@angular/compiler';
import {MockXHR} from '@angular/compiler/testing';

describe('Component: LanguagesListUpdateComponent', () => {
	let languagesListUpdateComponent: LanguagesListUpdateComponent;
	let connectionBackend: MockBackend;
	let element: any;
	let fixture: ComponentFixture<LanguagesListUpdateComponent>;

	let createResponse = (asyncResolve: boolean, _connectionBackend, method: RequestMethod, response?: Object,
		error?: Error) => {
		HttpClientTestHelper.createResponse({
			asyncResolve: asyncResolve,
			connectionBackend: _connectionBackend,
			unsubscribeAfter: true,
			method,
			error,
			url: '/api/admin/github/job/languages_list_update'
		}, response || {
			linguistLanguagesCount: 5,
			persistedLanguagesCount: 3,
			updatable: true
		});
	};

	beforeEachProviders(() => {
		return [
			HTTP_PROVIDERS,
			COMPILER_PROVIDERS,
			provide(XHR, { useClass: MockXHR }),
			provide(Router, { useClass: RootRouter }),
			provide(XHRBackend, { useClass: MockBackend }),
			provide(HttpClient, { useClass: HttpClient }),
			provide(LanguagesListUpdateComponent, { useClass: LanguagesListUpdateComponent }),
			provide(TestComponentBuilder, { useClass: TestComponentBuilder }),
			provide(LanguagesListUpdateApi, { useClass: LanguagesListUpdateApi }),
			provide(ConnectionBackend, { useClass: MockBackend }),
			provide(Http, { useClass: Http })
		];
	});

	beforeEach(injectAsync([TestComponentBuilder], tcb => {
		return tcb
			.createAsync(LanguagesListUpdateComponent)
			.then((_fixture) => {
				fixture = _fixture;
				languagesListUpdateComponent = fixture.componentInstance;
				element = fixture.nativeElement;
				fixture.detectChanges();
			});
	}));

	beforeEach(inject([ConnectionBackend], (_connectionBackend) => {
		connectionBackend = _connectionBackend;
	}));

	it('should be defined', () => {
		expect(languagesListUpdateComponent).not.toBe(undefined);
	});

	describe('status', () => {
		beforeEach(() => {
			spyOn(languagesListUpdateComponent['languagesListUpdateApi'], 'getStatus').and.callThrough();
			createResponse(true, connectionBackend, RequestMethod.Get);
		});

		it('can be refreshed', (done) => {
			expect(languagesListUpdateComponent['languagesListUpdateApi'].getStatus).not.toHaveBeenCalled();
			expect(languagesListUpdateComponent.updatable).toBe(undefined);

			languagesListUpdateComponent.refreshStatus();

			expect(languagesListUpdateComponent['languagesListUpdateApi'].getStatus).toHaveBeenCalled();
			setTimeout(() => {
				expect(languagesListUpdateComponent.updatable).not.toBe(undefined);
				done();
			});
		});
	});

	describe('can be triggered to run', () => {
		beforeEach(() => {
			spyOn(languagesListUpdateComponent['languagesListUpdateApi'], 'run').and.callThrough();
		});

		it('can be refreshed', (done) => {
			expect(languagesListUpdateComponent['languagesListUpdateApi'].run).not.toHaveBeenCalled();
			expect(languagesListUpdateComponent.updatable).toBe(undefined);

			createResponse(true, connectionBackend, RequestMethod.Post);
			languagesListUpdateComponent.run();

			expect(languagesListUpdateComponent['languagesListUpdateApi'].run).toHaveBeenCalled();
			setTimeout(() => {
				expect(languagesListUpdateComponent.updatable).toBe(true);
				expect(languagesListUpdateComponent.persistedLanguagesCount).toBe(3);
				expect(languagesListUpdateComponent.linguistLanguagesCount).toBe(5);
				done();
			});
		});

		it('should fail', (done) => {
			expect(languagesListUpdateComponent.updatable).toBe(undefined);

			createResponse(true, connectionBackend, RequestMethod.Post, undefined, new Error('Connection error.'));
			languagesListUpdateComponent.run();

			expect(languagesListUpdateComponent['languagesListUpdateApi'].run).toHaveBeenCalled();
			setTimeout(() => {
				expect(languagesListUpdateComponent.loading).toBe(false);
				expect(languagesListUpdateComponent.updatable).toBe(undefined);
				done();
			});
		});
	});
});
