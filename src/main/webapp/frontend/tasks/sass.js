module.exports = function(grunt) {
 	grunt.config.merge({
		sass: {
			dist: {
				options: {
					style: 'expanded'
				},
				files: [
					{
						src: 'sass/mainPage/index.sass',
						dest: 'dist/css/mainPage.css',
					},
					{
						src: 'sass/admin/index.sass',
						dest: 'dist/css/admin.css',
					}
				]
			}
		}
	});

	grunt.loadNpmTasks('grunt-contrib-sass');
};
