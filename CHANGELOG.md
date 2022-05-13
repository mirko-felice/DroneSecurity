# [0.3.0](https://github.com/mirko-felice/DroneSecurity/compare/0.2.0...0.3.0) (2022-05-13)


### Bug Fixes

* add waiting for script/command termination ([3146441](https://github.com/mirko-felice/DroneSecurity/commit/3146441eb6bb48213b61d40caad6896d5398b3ee))
* extend termination waiting time to DroneTest ([479a677](https://github.com/mirko-felice/DroneSecurity/commit/479a677d16d68439fabe218d40c0da4a2bf2021c))
* fix authentication not working when showing password ([a83ce7c](https://github.com/mirko-felice/DroneSecurity/commit/a83ce7c874d6f4cffeba8fa5ff8aea5a24423710))
* fix crash on connection window closing ([0e09d31](https://github.com/mirko-felice/DroneSecurity/commit/0e09d31f9893baf5aa62aa9a216241d0be597fdc))
* fix maintainer role ([5d45df2](https://github.com/mirko-felice/DroneSecurity/commit/5d45df2d099c7d211abdf4d914d7dd3a34276444))
* fix method name in fxml ([389fdbb](https://github.com/mirko-felice/DroneSecurity/commit/389fdbba35eb2d59574727442179ab5bee8727de))
* fix typo in CI ([83438e9](https://github.com/mirko-felice/DroneSecurity/commit/83438e9ec65732bc7891cffd6ef8642c47f8fdeb))
* fix username query string ([4769b73](https://github.com/mirko-felice/DroneSecurity/commit/4769b73b83221c5b6dc27977b9516553ff4e2104))
* re-interrupt thread when blocking deserialization goes wrong ([0a3ee8c](https://github.com/mirko-felice/DroneSecurity/commit/0a3ee8ccfa552947ead6e0984dcad54fd33ecc20))
* resolve spotbugs issues ([192a866](https://github.com/mirko-felice/DroneSecurity/commit/192a8664039143907042807518ebd7a55da6f81b))
* solve all merge issues ([acedb9c](https://github.com/mirko-felice/DroneSecurity/commit/acedb9c2fd9a3ac6186199dbdbbf511d7be4cc61))
* solve missed bugs ([29513c5](https://github.com/mirko-felice/DroneSecurity/commit/29513c5db41b687bfd25faade01deb514bf5c53c))
* update id assigning algorithm for issue ([7b588a3](https://github.com/mirko-felice/DroneSecurity/commit/7b588a3ca0a5a89421fbda4080420469047521e1))


### Features

* add closed issues visualization ([e959d9e](https://github.com/mirko-felice/DroneSecurity/commit/e959d9ee00dc914b52606009a02166fc38c72aa3))
* add data column in reports visualization ([fd54e99](https://github.com/mirko-felice/DroneSecurity/commit/fd54e99f35d4b9528b36acbed3942126c16e00db))
* add Issue reports creation ([4ed940a](https://github.com/mirko-felice/DroneSecurity/commit/4ed940a2f728007710b8161960c1e20312cdebde))
* add maintainer issues visualization ([0f87a04](https://github.com/mirko-felice/DroneSecurity/commit/0f87a048090faa9716e237b8b3d2647dead5f170))
* add negligence reports gui and controller ([733508f](https://github.com/mirko-felice/DroneSecurity/commit/733508fbae5ddb444ce5473a5439d71d7c430d57))
* add open issues visualization ([31460b0](https://github.com/mirko-felice/DroneSecurity/commit/31460b085e9ed691c8c2d8bb98308726c5533f9c))
* add supervisor to Courier ([c3f7755](https://github.com/mirko-felice/DroneSecurity/commit/c3f7755eab9333764eab5b8d008bac22fd493301))
* add take action on maintainer + major refactoring ([c072e35](https://github.com/mirko-felice/DroneSecurity/commit/c072e3528965f23256a6f9aef13f3362664ebae7))
* add the possibility for the maintainer to close an issue ([0f666f2](https://github.com/mirko-felice/DroneSecurity/commit/0f666f27967a7772d4dcb75487e1654eaa0ac0f3))
* add the possibility to vision an issue to maintainer ([dc99d4e](https://github.com/mirko-felice/DroneSecurity/commit/dc99d4e41b25ab078efce91814afdfc4fea3b590))
* create negligence reporting context in user-application ([4c6d1ec](https://github.com/mirko-felice/DroneSecurity/commit/4c6d1ec90a11ea113e7abaede02537a9953a196a))
* create negligence reporting in drone-system ([448a959](https://github.com/mirko-felice/DroneSecurity/commit/448a959b185d9c501574cf9b639d73467647a274))
* split service in courier and maintainer interfaces + refactoring ([4374ddd](https://github.com/mirko-felice/DroneSecurity/commit/4374ddd8277981ecaa4b5d035dee41001e2eb931))


### Performance Improvements

* create and execute alerts on javafx thread ([bf8cbe1](https://github.com/mirko-felice/DroneSecurity/commit/bf8cbe1242dff5da3a0ee2e3528c588515abcd09))
* improve reports table visualization ([f42b3a2](https://github.com/mirko-felice/DroneSecurity/commit/f42b3a2692015c1cfafb5633babc5b07df6ead19))
* start CourierShippingService only when logged in ([ce42645](https://github.com/mirko-felice/DroneSecurity/commit/ce426457e20f0c911f87345465364580e90484d7))
* throw exception when trying to close report without providing data ([a126770](https://github.com/mirko-felice/DroneSecurity/commit/a126770085675601cd1eab4b9c83fb7c83be9f94))
* update aws dependency + remove keep-alive ([4fd3626](https://github.com/mirko-felice/DroneSecurity/commit/4fd3626d2726469584617ee753688b82042f7135))

# [0.2.0](https://github.com/mirko-felice/DroneSecurity/compare/0.1.0...0.2.0) (2022-04-04)


### Bug Fixes

* correct wrong checks and generate properties at start-up if needed ([c8b2dc3](https://github.com/mirko-felice/DroneSecurity/commit/c8b2dc3e97e3f262e9331195955e8b321ebd4cda))
* fix client url connection ([b0ecd55](https://github.com/mirko-felice/DroneSecurity/commit/b0ecd55f6fafedc0cd0b308b85e5249eeedf128d))
* fix courier shipping service server creation ([bc688b0](https://github.com/mirko-felice/DroneSecurity/commit/bc688b09defef11c6df619fed321a24c47eb4911))
* fix courier shipping service url issue ([f32b25e](https://github.com/mirko-felice/DroneSecurity/commit/f32b25e6fca4239892f514db975bbeaf5eca308a))
* fix fat jar issues ([35ba443](https://github.com/mirko-felice/DroneSecurity/commit/35ba4436679dbaedb3c951248db4a59753e24ee4))
* resolve pmd issue ([334e62d](https://github.com/mirko-felice/DroneSecurity/commit/334e62d7a97073b21b6baa7af833146ef0a025c7))
* solve connection test issue if no certificates found ([f9bb8e4](https://github.com/mirko-felice/DroneSecurity/commit/f9bb8e480fa324c44f2ba0b13f53dbe5db9018ea))


### Features

* add accelerometer measurement and refactor sensor classes ([5f28dbf](https://github.com/mirko-felice/DroneSecurity/commit/5f28dbf443ca662b6b64ce652ad4d9e6a90af158))
* add base login feature ([1c61e7b](https://github.com/mirko-felice/DroneSecurity/commit/1c61e7b22463447c868788614e28d8270f4e78a6))
* add basic logic to show orders ([fe04007](https://github.com/mirko-felice/DroneSecurity/commit/fe040073ade5e7e6b7e28f1448ce22501b3e0a6d))
* add checkstyle, pmd and simple Order ([95f4c27](https://github.com/mirko-felice/DroneSecurity/commit/95f4c272a707fb1eafcd435eb3b09bd5f966ff93))
* add drone creation and change sensor structure ([a9e2140](https://github.com/mirko-felice/DroneSecurity/commit/a9e214008dd491f800c5ef33e31703be54a0d94c))
* add drone creation and comments ([e4c58e4](https://github.com/mirko-felice/DroneSecurity/commit/e4c58e474ed6cc281539184dc27130f5a103ed57))
* add drone lifecycle simulation ([94d6065](https://github.com/mirko-felice/DroneSecurity/commit/94d6065e13d7bc92000732b94e9359bf0de13ca0))
* add drone lifecycle tracking and perform minor refactorings ([03ffc75](https://github.com/mirko-felice/DroneSecurity/commit/03ffc75d87aeef8f61f1665d8b16c17e962cea99))
* add JavaFX ([feac47f](https://github.com/mirko-felice/DroneSecurity/commit/feac47ff64867607064d4e9088de138a4f79ca9e))
* add Order mapping configuration for Jackson deserializing ([8ab70aa](https://github.com/mirko-felice/DroneSecurity/commit/8ab70aa56529810bab7fd8dfe33df82502f724d8))
* add password hashing + toggle visibility with icon ([fdfa3cf](https://github.com/mirko-felice/DroneSecurity/commit/fdfa3cf5a7513639b7f0b1a2b2e2ccc923c41a41))
* add python test script and establish MQTT connection with AWS ([1320f34](https://github.com/mirko-felice/DroneSecurity/commit/1320f349471d1fd640c593a6fa4dc11a235a0b65))
* add rescheduleDelivery Handler ([33efd4c](https://github.com/mirko-felice/DroneSecurity/commit/33efd4c354d670320d3feea88ec6970f16556e97))
* add rescheduleDelivery to CourierShippingService specification ([a667ed9](https://github.com/mirko-felice/DroneSecurity/commit/a667ed94073723a5a2111bd68267a46353a890e2))
* add sensor creation ([bc27cb8](https://github.com/mirko-felice/DroneSecurity/commit/bc27cb8d18c32cc7441cbba1a2f73f36cdfd6031))
* add the visualization of data published by the drone ([7bb6db7](https://github.com/mirko-felice/DroneSecurity/commit/7bb6db7a446216c1576dbe667c1bfe74e815dce5))
* **courierService:** create Service based on OpenAPI specification ([09401b2](https://github.com/mirko-felice/DroneSecurity/commit/09401b2d88b1023fb40f9ad5d7a4566375a6644c))
* create connection settings initializer ([77982c7](https://github.com/mirko-felice/DroneSecurity/commit/77982c7d9ecf95c49441b07f5efd3015a866992c))
* create MonitorController + Alert utility class ([461118f](https://github.com/mirko-felice/DroneSecurity/commit/461118f0d9299456a75573f78fa1747e272595cd))
* create sensor data analyzer + minor refactoring ([8c4c997](https://github.com/mirko-felice/DroneSecurity/commit/8c4c9970ccf67c2afa402140aa25c19fbeb63f88))
* **order:** create repository (using fake generation) to get orders ([38acc0f](https://github.com/mirko-felice/DroneSecurity/commit/38acc0fcccd744ddb126c6a9e7be21e76ef3096b))
* **order:** create state lifecycle ([f6f7951](https://github.com/mirko-felice/DroneSecurity/commit/f6f79516d93caa2571b577075985807210d003f7))


### Performance Improvements

* resolve sonarcloud code quality issues ([6284116](https://github.com/mirko-felice/DroneSecurity/commit/6284116da5045759244e8d98815e6bef38357cc8))
