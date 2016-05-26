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
import {SchemaUpdateComponent} from './schemaUpdateComponent';
import {SchemaUpdateApi} from './schemaUpdateApi';
import {COMPILER_PROVIDERS, XHR} from '@angular/compiler';
import {MockXHR} from '@angular/compiler/testing';

describe('Component: SchemaUpdateComponent', () => {
	const LINGUIST_VERSION = '4.7.5';
	let schemaUpdateComponent: SchemaUpdateComponent;
	let connectionBackend: MockBackend;
	let element: any;
	let fixture: ComponentFixture<SchemaUpdateComponent>;

	let createResponse = (asyncResolve: boolean, _connectionBackend, method: RequestMethod, error?: Error) => {
		HttpClientTestHelper.createResponse({
			asyncResolve: asyncResolve,
			connectionBackend: _connectionBackend,
			unsubscribeAfter: true,
			method,
			error,
			url: '/api/admin/github/job/schema_update'
		}, {
			enabled: false,
			linguistVersion: LINGUIST_VERSION,
			running: false,
			updated: new Date()
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
			provide(SchemaUpdateComponent, { useClass: SchemaUpdateComponent }),
			provide(TestComponentBuilder, { useClass: TestComponentBuilder }),
			provide(SchemaUpdateApi, { useClass: SchemaUpdateApi }),
			provide(ConnectionBackend, { useClass: MockBackend }),
			provide(Http, { useClass: Http })
		];
	});

	beforeEach(injectAsync([TestComponentBuilder], tcb => {
		return tcb
			.createAsync(SchemaUpdateComponent)
			.then((_fixture) => {
				fixture = _fixture;
				schemaUpdateComponent = fixture.componentInstance;
				element = fixture.nativeElement;
				fixture.detectChanges();
			});
	}));

	beforeEach(inject([ConnectionBackend], (_connectionBackend) => {
		connectionBackend = _connectionBackend;
	}));

	it('should be defined', () => {
		expect(schemaUpdateComponent).not.toBe(undefined);
	});

	describe('status', () => {
		beforeEach(() => {
			spyOn(schemaUpdateComponent['schemaUpdateApi'], 'getStatus').and.callThrough();
			createResponse(true, connectionBackend, RequestMethod.Get);
		});

		it('can be refreshed', (done) => {
			expect(schemaUpdateComponent['schemaUpdateApi'].getStatus).not.toHaveBeenCalled();
			expect(schemaUpdateComponent.updated).toBe(undefined);

			schemaUpdateComponent.refreshStatus();

			expect(schemaUpdateComponent['schemaUpdateApi'].getStatus).toHaveBeenCalled();
			setTimeout(() => {
				expect(schemaUpdateComponent.updated).not.toBe(undefined);
				done();
			});
		});
	});

	describe('can be triggered to run', () => {
		beforeEach(() => {
			spyOn(schemaUpdateComponent['schemaUpdateApi'], 'run').and.callThrough();
		});

		it('can be refreshed', (done) => {
			expect(schemaUpdateComponent['schemaUpdateApi'].run).not.toHaveBeenCalled();
			expect(schemaUpdateComponent.updated).toBe(undefined);

			createResponse(true, connectionBackend, RequestMethod.Post);
			schemaUpdateComponent.run();

			expect(schemaUpdateComponent['schemaUpdateApi'].run).toHaveBeenCalled();
			setTimeout(() => {
				expect(schemaUpdateComponent.updated).not.toBe(undefined);
				expect(schemaUpdateComponent.running).toBe(false);
				expect(schemaUpdateComponent.enabled).toBe(false);
				expect(schemaUpdateComponent.linguistVersion).toBe(LINGUIST_VERSION);
				done();
			});
		});

		it('should fail', (done) => {
			expect(schemaUpdateComponent.updated).toBe(undefined);

			createResponse(true, connectionBackend, RequestMethod.Post, new Error('Connection error.'));
			schemaUpdateComponent.run();

			expect(schemaUpdateComponent['schemaUpdateApi'].run).toHaveBeenCalled();
			setTimeout(() => {
				expect(schemaUpdateComponent.loading).toBe(false);
				expect(schemaUpdateComponent.updated).toBe(undefined);
				done();
			});
		});
	});
});
