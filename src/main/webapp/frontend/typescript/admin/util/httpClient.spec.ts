import {it, describe, expect, beforeEach, inject, beforeEachProviders} from 'angular2/testing';
import {MockBackend} from 'angular2/http/testing';
import {Router} from 'angular2/router';
import {HTTP_PROVIDERS, XHRBackend, Http, ConnectionBackend} from 'angular2/http';
import {provide} from 'angular2/core';
import {RootRouter} from 'angular2/src/router/router';
import {HttpClient} from './httpClient';
import {HttpClientTestHelper} from './httpClientTestHelper.spec';

describe('Service: HttpClient', () => {
	let httpClient: HttpClient;

	beforeEachProviders(() => {
		return [
			HTTP_PROVIDERS,
			provide(Router, {useClass: RootRouter}),
			provide(XHRBackend, {useClass: MockBackend}),
			provide(HttpClient, {useClass: HttpClient}),
			provide(ConnectionBackend, {useClass: MockBackend}),
			provide(Http, {useClass: Http})
		];
	});

	beforeEach(inject([HttpClient, ConnectionBackend], (_httpClient, connectionBackend) => {
		httpClient = _httpClient;
		HttpClientTestHelper.createResponse({ connectionBackend, url: '/api/user/get' }, {});
	}));

	it('should be defined', () => {
		expect(httpClient).not.toBe(undefined);
	});

	it('performs valid GET', done => {
		httpClient.get('api/user/get').then(() => {
			expect(true).toBe(true);
			done();
		}).catch(() => {
			expect(false).toBe(true);
			done();
		});
	});

	it('reports invalid GET', done => {
		httpClient.get('invalid/path').then(() => {
			expect(false).toBe(true);
			done();
		}).catch(() => {
			expect(true).toBe(true);
			done();
		});
	});
});
