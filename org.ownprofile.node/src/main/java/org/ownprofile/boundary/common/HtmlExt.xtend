package org.ownprofile.boundary.common

import java.net.URI

class HtmlExt {
	
	def html(CharSequence bodyContent, CharSequence title, CharSequence header) '''
		<html>
		<head>
			<title>«title»</title>
«««			<meta http-equiv="refresh" content="2" />
		</head>
		<body>
		«header»
		«title.h1»
		«bodyContent»
		</body>
		</html>
	'''
	
	def link(URI uri, CharSequence linkText) {
		uri.toString.link(linkText)
	} 
	
	def link(CharSequence href, CharSequence linkText) '''<a href="«href»">«linkText»</a>'''
	
	def h1(CharSequence text) '''<h1>«text»</h1>'''
	def h2(CharSequence text) '''<h2>«text»</h2>'''
	
	def ul(Iterable<? extends CharSequence> entries) '''
		<ul>
		«FOR entry : entries»<li>«entry»</li>«ENDFOR»
		</ul>
	'''
	
	def concat(Iterable<? extends CharSequence> entries) '''
		«FOR entry : entries»«entry»«ENDFOR»
	'''
	
	def p(CharSequence content) '''<p>«content»</p>'''
}