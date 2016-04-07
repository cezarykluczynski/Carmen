import {it, describe, expect, beforeEach, inject, beforeEachProviders} from 'angular2/testing';
import {MockBackend} from 'angular2/http/testing';
import {Router} from 'angular2/router';
import {HTTP_PROVIDERS, XHRBackend, Http, ConnectionBackend, RequestMethod} from 'angular2/http';
import {provide} from 'angular2/core';
import {RootRouter} from 'angular2/src/router/router';
import {HttpClient} from './httpClient';
import {HttpClientTestHelper} from './httpClientTestHelper.spec';

describe('Service: HttpClient', () => {
	let httpClient: HttpClient;
	let connectionBackend: MockBackend;

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

	beforeEach(inject([HttpClient, ConnectionBackend], (_httpClient, _connectionBackend) => {
		httpClient = _httpClient;
		connectionBackend = _connectionBackend;
	}));

	it('should be defined', () => {
		expect(httpClient).not.toBe(undefined);
	});

	describe('GET', () => {
		beforeEach(() => {
			HttpClientTestHelper.createResponse({
				connectionBackend,
				url: '/api/user/get'
			}, {});
		});

		it('performs valid request', done => {
			httpClient.get('api/user/get').then(() => {
				expect(true).toBe(true);
				done();
			}).catch(() => {
				expect(false).toBe(true);
				done();
			});
		});

		it('reports invalid request', done => {
			httpClient.get('invalid/path').then(() => {
				expect(false).toBe(true);
				done();
			}).catch(() => {
				expect(true).toBe(true);
				done();
			});
		});
	});

	describe('POST', () => {
		beforeEach(() => {
			HttpClientTestHelper.createResponse({
				connectionBackend,
				method: RequestMethod.Post,
				url: '/api/user/post'
			}, {});
		});

		it('performs valid request', done => {
			httpClient.post('api/user/post', {}).then(() => {
				expect(true).toBe(true);
				done();
			}).catch(() => {
				expect(false).toBe(true);
				done();
			});
		});

		it('reports invalid request', done => {
			httpClient.post('invalid/path', {}).then(() => {
				expect(false).toBe(true);
				done();
			}).catch(() => {
				expect(true).toBe(true);
				done();
			});
		});

	});
});
