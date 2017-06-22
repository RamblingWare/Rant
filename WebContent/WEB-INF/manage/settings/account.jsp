<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false"
	errorPage="/WEB-INF/error/error.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/fragment/meta-manage.jspf"%>

<title>Account - RamblingWare</title>
</head>
<body class="w3-theme-dark">

	<!-- HEADER_BEGIN -->
	<%@include file="/WEB-INF/fragment/header.jspf"%>
	<!-- HEADER_END -->
	
	<article class="w3-theme-light">
		<div class="page w3-row">
		
			<!-- TABS_BEGIN -->
			<%@include file="/WEB-INF/manage/settings/settings-tabs.jspf"%>
			<!-- TABS_END -->
		
			<div id="page-content" class="w3-col m8 l8 w3-container w3-padding">
				
				<h1>Account</h1>
				
				<!-- SETTINGS BEGIN -->
				<div class="w3-row">	
				<div class="w3-container w3-padding w3-col s12 m12 l6">
				
					<div class="w3-border w3-round">
						<div class="w3-margin-0 w3-padding-0 w3-theme-light w3-center">
							<h3 class="w3-margin-0 w3-padding uppercase">Account</h3>
						</div>
						<div class="w3-padding w3-small w3-theme-light">
						
							<form action="/manage/settings/account" method="post">
							<input type="hidden" name="account" value="true" />
							<p>
								<label class="w3-validate w3-text-grey-light w3-large" for="username">Username:&nbsp;<span class="w3-text-red">*</span></label>
								<input type="text" size="50" maxlength="200" name="username" id="username" value="<s:property value="#session.USER.username" />" required class="w3-input w3-round-large w3-border" />
								<span class="w3-small w3-text-grey">Your username is used to login.</span>
							</p>
							<p>
								<label class="w3-validate w3-text-grey-light w3-large" for="email">Email:&nbsp;<span class="w3-text-red">*</span></label>
								<input type="text" size="50" maxlength="200" name="email" id="email" value="<s:property value="#session.USER.email" />" required class="w3-input w3-round-large w3-border" />
								<span class="w3-small w3-text-grey">Your email address is used to validate your identity.</span>
							</p>
							<p>
								<s:if test="#session.USER.isAdmin()">
									<label class="w3-validate w3-text-grey-light w3-large" for="title">Role:</label>&nbsp;<span class="w3-tag w3-round w3-theme">Admin</span>
									<br/>
									<span class="w3-small w3-text-grey">Admins have full access.</span>
								</s:if>
								<s:else>
									<label class="w3-validate w3-text-grey-light w3-large" for="title">Role:</label>&nbsp;<span class="w3-tag w3-round w3-pale-blue">Author</span>
									<br/>
									<span class="w3-small w3-text-grey">Authors can create/edit blog posts.</span>
								</s:else>
							</p>
							<hr />
							<p>
								<button class="w3-btn w3-round w3-card w3-pale-green" type="submit" value="Save" title="Save Changes">Save Changes</button>
							</p>
							</form>
						</div>
					</div>
				
				</div>
				</div>
				<!-- SETTINGS END -->
			
								
				<br />
				<br />
			</div>
		</div>
	</article>
	
	<!-- FOOTER_BEGIN -->
	<%@include file="/WEB-INF/fragment/footer.jspf"%>
	<!-- FOOTER_END -->
	
</body>
</html>