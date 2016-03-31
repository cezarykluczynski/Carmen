module.exports = function(config) {
	config.set({
	basePath: '.',
		frameworks: ['jasmine'],
		files: [
			{pattern: 'node_modules/es6-shim/es6-shim.js', included: true, watched: true},
			{pattern: 'node_modules/systemjs/dist/system-polyfills.js', included: true, watched: true},
			{pattern: 'node_modules/angular2/es6/dev/src/testing/shims_for_IE.js', included: true, watched: true},
			{pattern: 'node_modules/angular2/bundles/angular2-polyfills.js', included: true, watched: true},
			{pattern: 'node_modules/systemjs/dist/system.src.js', included: true, watched: true},
			{pattern: 'node_modules/rxjs/bundles/Rx.js', included: true, watched: true},
			{pattern: 'node_modules/angular2/bundles/angular2.dev.js', included: true, watched: true},
			{pattern: 'node_modules/angular2/bundles/testing.dev.js', included: true, watched: true},
			{pattern: 'node_modules/angular2/bundles/router.js', included: true, watched: true},
			{pattern: 'node_modules/angular2/bundles/http.dev.js', included: true, watched: true},
			{pattern: 'node_modules/jquery/dist/jquery.js', included: true, watched: true},
			{pattern: 'karma-test-shim.js', included: true, watched: true},
			{pattern: 'dist/javascript/**/*.js', included: false, watched: true},
			{pattern: 'typescript/**/*.ts', included: false, watched: true},
			{pattern: 'dist/javascript/**/*.js.map', included: false, watched: false}
		],
		proxies: {
			'/src/': '/base/src/'
		},
		port: 9876,
		logLevel: config.LOG_INFO,
		colors: true,
		autoWatch: true,
		browsers: ['PhantomJS'],
		plugins: [
			'karma-jasmine',
			'karma-coverage',
			'karma-phantomjs-launcher',
			'karma-chrome-launcher'
		],
		reporters: ['progress', 'coverage'],
		preprocessors: {
			'dist/javascript/**/!(*spec).js': ['coverage']
		},
		coverageReporter: {
		  dir: 'coverage',
		  reporters: [
			 {
			 	type: 'json',
			 	subdir: '.',
			 	file: 'coverage-js.json'
			 }
		  ]
		},
		singleRun: true
	})
};
