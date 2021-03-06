module.exports = function(config) {
	config.set({
	basePath: '.',
		frameworks: ['jasmine'],
		files: [
			{pattern: 'dist/javascript/vendor.js', included: true, watched: true},
			{pattern: 'node_modules/@angular/**/*.js', included: false, watched: false},
			{pattern: 'node_modules/rxjs/**/*.js', included: false, watched: false},
			{pattern: 'node_modules/zone.js/dist/async-test.js', included: true, watched: true},
			{pattern: 'node_modules/zone.js/dist/fake-async-test.js', included: true, watched: true},
			{pattern: 'node_modules/jquery/dist/jquery.js', included: true, watched: true},
			{pattern: 'karma-conf.js', included: false, watched: true},
			{pattern: 'karma-test-shim.js', included: true, watched: true},
			{pattern: 'dist/javascript/**/*.js', included: false, watched: true},
			{pattern: 'typescript/**/*.ts', included: false, watched: true}
		],
		proxies: {
			'/src/': '/base/src/'
		},
		port: 9876,
		logLevel: config.LOG_INFO,
		colors: true,
		autoWatch: false,
		singleRun: true,
		browsers: [
			'PhantomJS',
			// 'Chrome'
		],
		plugins: [
			'karma-jasmine',
			'karma-coverage',
			'karma-phantomjs-launcher',
			'karma-chrome-launcher'
		],
		reporters: ['progress', 'coverage'],
		preprocessors: {
			'dist/javascript/**/!(*spec|vendor).js': ['coverage']
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
	})
};
