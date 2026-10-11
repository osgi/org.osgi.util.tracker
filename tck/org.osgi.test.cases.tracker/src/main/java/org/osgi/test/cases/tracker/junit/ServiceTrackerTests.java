/*******************************************************************************
 * Copyright (c) Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0 
 *******************************************************************************/

package org.osgi.test.cases.tracker.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Hashtable;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.test.cases.tracker.service.TestService1;
import org.osgi.test.cases.tracker.service.TestService2;
import org.osgi.test.cases.tracker.service.TestService3;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectBundleInstaller;
import org.osgi.test.common.install.BundleInstaller;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ServiceTrackerTests {

	@InjectBundleContext
	BundleContext		context;

	@InjectBundleInstaller
	BundleInstaller		installer;

	String				testMethodName;

	@BeforeEach
	void testMethodName(TestInfo testInfo) {
		testMethodName = testInfo.getTestMethod().get().getName();
	}

	@Test
	public void testOpenClose() throws Exception {
		// 2.23.1 Testcase1 (tc1), tracking a service
		// Tb1 contains service: testservice1
		// Install tb1
		Bundle tb = installer.installBundle("tb1.jar");

		// Creates ServiceTracker object with ServiceReference to testservice1
		ServiceReference<TestService1> sr = context
				.getServiceReference(TestService1.class);
		ServiceTracker<TestService1, TestService1> st = new ServiceTracker<TestService1, TestService1>(
				context, sr, null);
		st.open();

		try {
			// Call ServiceTracker.size()
			// Should reply 1
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker is: 1").isOne();

			// Call ServiceTracker.getServiceReferences()
			ServiceReference<TestService1>[] srs = st.getServiceReferences();
			assertThat(srs).as("ServiceReference for the tracked service can be reached at this time: true").isNotNull();
			assertThat(srs.length).as("ServiceReference for the tracked service can be reached at this time: 1").isOne();
			// Call ServiceTracker.getService()
			TestService1 ss = st.getService();
			// Call ServiceTracker.getServices()
			Object[] sss = st.getServices();
			// Call ServiceTracker.getService(ServiceReference)
			TestService1 ssr = st.getService(sr);
			// All should be equal and testservice1
			assertThat(sss[0]).as("Tracked services can be reached at this time and are equal in the different methods").isSameAs(ss);
			assertThat(ssr).as("Tracked services can be reached at this time and are equal in the different methods").isSameAs(ss);

			// Call ServiceTracker.close()
			st.close();

			// Call ServiceTracker.getServiceReferences()
			srs = st.getServiceReferences();
			assertThat(srs).as("No ServiceReferences for tracked services can be reached at this time").isNull();
			// Call ServiceTracker.getService()
			ss = st.getService();
			// Call ServiceTracker.getServices()
			sss = st.getServices();
			// Call ServiceTracker.getService(ServiceReference)
			ssr = st.getService(sr);
			// All should be null
			assertThat(ss).as("No Services for tracked services can be reached at this time: true").isNull();
			assertThat(sss).as("No Services for tracked services can be reached at this time: true").isNull();
			assertThat(ssr).as("No Services for tracked services can be reached at this time: true").isNull();

			// Call ServiceTracker.size()
			// Should reply 0
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker is: 0 ").isZero();

			st.open();

			// Call ServiceTracker.size()
			// Should reply 1
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker is: 1").isOne();

			tb.uninstall();
			tb = null;
			// Call ServiceTracker.getServiceReferences()
			srs = st.getServiceReferences();
			assertThat(srs).as("No ServiceReferences for tracked services can be reached at this time: true").isNull();
			// Call ServiceTracker.getService()
			ss = st.getService();
			// Call ServiceTracker.getServices()
			sss = st.getServices();
			// Call ServiceTracker.getService(ServiceReference)
			ssr = st.getService(sr);
			// Should reply with null
			assertThat(ss).as("No Services for tracked services can be reached at this time: true").isNull();
			assertThat(sss).as("No Services for tracked services can be reached at this time: true").isNull();
			assertThat(ssr).as("No Services for tracked services can be reached at this time: true").isNull();

			// Call ServiceTracker.size()
			// Should reply 0
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker is: 0 ").isZero();
		}
		finally {
			st.close();
			if (tb != null) {
				tb.uninstall();
			}
		}
	}

	// add testing for isEmpty and getServices(T[])
	@Test
	public void testOpenClose2() throws Exception {
		TestService1 dummy = new TestService1() {};
		// 2.23.1 Testcase1 (tc1), tracking a service
		// Tb1 contains service: testservice1
		// Install tb1
		Bundle tb = installer.installBundle("tb1.jar");

		// Creates ServiceTracker object with ServiceReference to testservice1
		ServiceReference<TestService1> sr = context
				.getServiceReference(TestService1.class);
		ServiceTracker<TestService1, TestService1> st = new ServiceTracker<TestService1, TestService1>(
				context, sr, null);
		st.open();

		try {
			// Call ServiceTracker.size()
			// Should reply 1
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker is: 1").isOne();
			// Call ServiceTracker.isEmpty()
			// Should reply false
			assertThat(st.isEmpty()).as("The ServiceTracker is empty").isFalse();

			// Call ServiceTracker.getServiceReferences()
			ServiceReference<TestService1>[] srs = st.getServiceReferences();
			assertThat(srs).as("ServiceReference for the tracked service can be reached at this time: true").isNotNull();
			assertThat(srs.length).as("ServiceReference for the tracked service can be reached at this time: 1").isOne();
			// Call ServiceTracker.getService()
			TestService1 ss = st.getService();
			assertThat(ss).isNotNull();
			// Call ServiceTracker.getServices()
			Object[] sss = st.getServices();
			assertThat(sss).isNotNull();
			// Call ServiceTracker.getServices(T[])
			TestService1[] ssst = new TestService1[st.size()];
			TestService1[] ssstr = st.getServices(ssst);
			assertThat(ssstr).as("different array returned").isSameAs(ssst);
			ssst = new TestService1[0];
			ssstr = st.getServices(ssst);
			assertThat(ssstr).as("same array returned").isNotSameAs(ssst);
			assertThat(ssstr.length).as("wrong size array").isEqualTo(st.size());
			ssst = new TestService1[st.size() + 10];
			ssst[st.size()] = dummy;
			ssstr = st.getServices(ssst);
			assertThat(ssstr).as("different array returned").isSameAs(ssst);
			assertThat(ssstr[st.size()]).as("no null after last element").isNull();
			// Call ServiceTracker.getService(ServiceReference)
			TestService1 ssr = st.getService(sr);
			// All should be equal and testservice1
			assertThat(sss[0]).as("Tracked services can be reached at this time and are equal in the different methods").isSameAs(ss);
			assertThat(ssstr[0]).as("Tracked services can be reached at this time and are equal in the different methods").isSameAs(ss);
			assertThat(ssr).as("Tracked services can be reached at this time and are equal in the different methods").isSameAs(ss);

			// Call ServiceTracker.close()
			st.close();

			// Call ServiceTracker.getServiceReferences()
			srs = st.getServiceReferences();
			assertThat(srs).as("No ServiceReferences for tracked services can be reached at this time").isNull();
			// Call ServiceTracker.getService()
			ss = st.getService();
			assertThat(ss).as("No Services for tracked services can be reached at this time: true").isNull();
			// Call ServiceTracker.getServices()
			sss = st.getServices();
			assertThat(sss).as("No Services for tracked services can be reached at this time: true").isNull();
			// Call ServiceTracker.getServices(T[])
			ssst = new TestService1[st.size() + 10];
			ssst[st.size()] = dummy;
			ssstr = st.getServices(ssst);
			assertThat(ssstr).as("different array returned").isSameAs(ssst);
			assertThat(ssstr[st.size()]).as("no null after last element").isNull();
			ssst = new TestService1[st.size()];
			ssstr = st.getServices(ssst);
			assertThat(ssstr).as("different array returned").isSameAs(ssst);
			assertThat(ssstr.length).as("No Services for tracked services can be reached at this time: true").isZero();
			// Call ServiceTracker.getService(ServiceReference)
			ssr = st.getService(sr);
			// All should be null
			assertThat(ssr).as("No Services for tracked services can be reached at this time: true").isNull();

			// Call ServiceTracker.size()
			// Should reply 0
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker is: 0 ").isZero();
			// Should reply true
			assertThat(st.isEmpty()).as("The number of Services being tracked by ServiceTracker is: 0 ").isTrue();

			st.open();

			// Call ServiceTracker.size()
			// Should reply 1
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker is: 1").isOne();
			// Call ServiceTracker.isEmpty()
			// Should reply false
			assertThat(st.isEmpty()).as("The ServiceTracker is empty").isFalse();

			tb.uninstall();
			tb = null;
			// Call ServiceTracker.getServiceReferences()
			srs = st.getServiceReferences();
			assertThat(srs).as("No ServiceReferences for tracked services can be reached at this time: true").isNull();
			// Call ServiceTracker.getService()
			ss = st.getService();
			assertThat(ss).as("No Services for tracked services can be reached at this time: true").isNull();
			// Call ServiceTracker.getServices()
			sss = st.getServices();
			assertThat(sss).as("No Services for tracked services can be reached at this time: true").isNull();
			// Call ServiceTracker.getServices(T[])
			ssst = new TestService1[st.size() + 10];
			ssst[st.size()] = dummy;
			ssstr = st.getServices(ssst);
			assertThat(ssstr).as("different array returned").isSameAs(ssst);
			assertThat(ssstr[st.size()]).as("no null after last element").isNull();
			ssst = new TestService1[st.size()];
			ssstr = st.getServices(ssst);
			assertThat(ssstr).as("different array returned").isSameAs(ssst);
			assertThat(ssstr.length).as("No Services for tracked services can be reached at this time: true").isZero();
			// Call ServiceTracker.getService(ServiceReference)
			ssr = st.getService(sr);
			assertThat(ssr).as("No Services for tracked services can be reached at this time: true").isNull();

			// Call ServiceTracker.size()
			// Should reply 0
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker is: 0 ").isZero();
			// Should reply true
			assertThat(st.isEmpty()).as("The number of Services being tracked by ServiceTracker is: 0 ").isTrue();
		}
		finally {
			st.close();
			if (tb != null) {
				tb.uninstall();
			}
		}
	}

	@Test
	public void testWaitForService() throws Exception {

		BundleContext context = this.context;

		// 2.23.2 Testcase2 (tc2), waitforService
		// Tb1 contains service: testservice1
		// Tb2 contains service: testservice2
		// Tb3 contains service: testservice3
		// Install tb1, tb2 and tb3
		Bundle b1 = installer.installBundle("tb1.jar", false);
		Bundle b2 = installer.installBundle("tb2.jar", false);
		Bundle b3 = installer.installBundle("tb3.jar", false);

		// Creates ServiceTracker1 object with testservice1
		// Call ServiceTracker.open()
		ServiceTracker<TestService1, TestService1> st1 = new ServiceTracker<TestService1, TestService1>(
				context, TestService1.NAME, null);
		st1.open();
		// Creates ServiceTracker2 object with testservice2
		// Call ServiceTracker.open()
		ServiceTracker<TestService2, TestService2> st2 = new ServiceTracker<TestService2, TestService2>(
				context, TestService2.NAME, null);
		st2.open();
		// Creates ServiceTracker3 object with testservice3
		// Call ServiceTracker.open()
		ServiceTracker<TestService3, TestService3> st3 = new ServiceTracker<TestService3, TestService3>(
				context, TestService3.NAME, null);
		st3.open();

		try {
			// Call ServiceTracker.size()
			assertThat(st1.size()).as("The number of Services being tracked by ServiceTracker 1 is: 0").isZero();
			// Call ServiceTracker.size()
			assertThat(st2.size()).as("The number of Services being tracked by ServiceTracker 2 is: 0").isZero();
			// Call ServiceTracker.size()
			assertThat(st3.size()).as("The number of Services being tracked by ServiceTracker 3 is: 0").isZero();

			Semaphore s1 = new Semaphore(0);
			BundleStarter t1 = new BundleStarter(b1, s1);
			t1.start();
			s1.release();
			TestService1 tt1 = st1.waitForService(0);
			assertThat(tt1).as("Returned an object in ServiceTracker 1?:  true").isNotNull();
			assertThat(st1.size()).as("The number of Services being tracked by ServiceTracker 1 is: 1").isOne();
			assertThat(st2.size()).as("The number of Services being tracked by ServiceTracker 2 is: 0").isZero();
			assertThat(st3.size()).as("The number of Services being tracked by ServiceTracker 3 is: 0").isZero();

			Semaphore s2 = new Semaphore(0);
			BundleStarter t2 = new BundleStarter(b2, s2);
			t2.start();
			TestService2 tt2 = st2.waitForService(1000);
			assertThat(tt2).as("Returned an object in ServiceTracker 2?: false").isNull();
			assertThat(st1.size()).as("The number of Services being tracked by ServiceTracker 1 is: 1").isOne();
			assertThat(st2.size()).as("The number of Services being tracked by ServiceTracker 2 is: 0").isZero();
			assertThat(st3.size()).as("The number of Services being tracked by ServiceTracker 3 is: 0").isZero();
			s2.release();

			Semaphore s3 = new Semaphore(1);
			BundleStarter t3 = new BundleStarter(b3, s3);
			t3.start();

			// wait for threads to complete
			st2.waitForService(5000);
			st3.waitForService(5000);

			assertThat(st1.size()).as("The number of Services being tracked by ServiceTracker 1 is: 1").isOne();
			assertThat(st2.size()).as("The number of Services being tracked by ServiceTracker 2 is: 1").isOne();
			assertThat(st3.size()).as("The number of Services being tracked by ServiceTracker 3 is: 1").isOne();
		}
		finally {
			// Call ServiceTracker.close()
			st1.close();
			st2.close();
			st3.close();

			b3.uninstall();
			b2.uninstall();
			b1.uninstall();
		}
	}

	private class BundleStarter extends Thread {
		private final Semaphore	semaphore;
		private final Bundle	bundle;

		BundleStarter(Bundle bundle, Semaphore semaphore) {
			this.bundle = bundle;
			this.semaphore = semaphore;
		}

		public void run() {
			try {
				semaphore.acquire();
				bundle.start();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testCustomizer() throws Exception {
		BundleContext context = this.context;
		Bundle tb;
		// 2.23.3 Testcase3 (tc3), ServiceTrackerCustomizer
		// Tb1 contains service: testservice1
		// Implement ServiceTrackerCustomizer

		// Create ServiceTracker object with testservice1
		// Call ServiceTracker.open()
		final boolean[] customizerCalled = new boolean[] {false, false, false};
		ServiceTracker<TestService1, TestService1> st = new ServiceTracker<TestService1, TestService1>(
				context, TestService1.class.getName(),
				new ServiceTrackerCustomizer<TestService1, TestService1>() {
					public TestService1 addingService(
							ServiceReference<TestService1> reference) {
						synchronized (customizerCalled) {
							customizerCalled[0] = true;
						}
						TestService1 obj = context.getService(reference);
						return obj;

					}

					public void modifiedService(
							ServiceReference<TestService1> reference,
							TestService1 service) {
						synchronized (customizerCalled) {
							customizerCalled[1] = true;
						}
					}

					public void removedService(
							ServiceReference<TestService1> reference,
							TestService1 service) {
						synchronized (customizerCalled) {
							customizerCalled[2] = true;
						}
						context.ungetService(reference);
					}
				});
		st.open();
		try {
			// Call ServiceTracker.size()
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 0").isZero();
			// Install tb1
			tb = installer.installBundle("tb1.jar");
			try {
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("addingService not called").isTrue();
				}
				assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 1").isOne();
				// Addingservice should do something

			}
			finally {
				// Uninstall tb1
				tb.uninstall();
			}
			// RemovedService should do something
			synchronized (customizerCalled) {
				assertThat(customizerCalled[2]).as("removedService not called").isTrue();
			}
		}
		finally {
			// Call ServiceTracker.close()
			st.close();
		}

	}

	@Test
	public void testRemove() throws Exception {
		BundleContext context = this.context;

		// 2.23.4 Testcase4 (tc4), tracking a classname
		// Tb1 contains service: testservice1, testservice2, testservice3
		// Tb2 contains service: testservice1
		// Tb3 contains service: testservice1
		// Tb4 uses testservice2
		// Creates ServiceTracker object with classname testservice1
		// Call ServiceTracker.open()
		Filter f = context.createFilter("(name=TestService1)");
		ServiceTracker<TestService1, TestService1> st = new ServiceTracker<TestService1, TestService1>(
				context, f, null);
		st.open();

		// Install tb1, tb2, tb3 and tb4
		Bundle tb1 = installer.installBundle("tb1.jar");
		Bundle tb2 = installer.installBundle("tb2.jar");
		Bundle tb3 = installer.installBundle("tb3.jar");
		Bundle tb4 = installer.installBundle("tb4.jar");

		try {
			// Call ServiceTracker.getServiceReferences()
			ServiceReference<TestService1>[] srs = st.getServiceReferences();
			assertThat(srs).as("one bundle registered TestService1").isNotNull();
			assertThat(srs.length).as("one bundle registered TestService1").isOne();
			assertThat(srs[0].getBundle().getBundleId()).as("tb1 registered TestService1").isEqualTo(tb1.getBundleId());
			// Call ServiceTracker.getServices()
			Object[] os = st.getServices();
			assertThat(os).as("one registered TestService1").isNotNull();
			assertThat(os.length).as("one registered TestService1").isOne();
			assertThat(os[0] instanceof TestService1).as("instanceof TestService1").isTrue();

			// Call ServiceTracker.size()
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 1").isOne();

			ServiceReference<TestService1> sr = context
					.getServiceReference(TestService1.class);
			st.remove(sr);
			// Call ServiceTracker.getServiceReferences()
			// Should find tb1, tb3
			srs = st.getServiceReferences();
			assertThat(srs).as("no TestService1").isNull();

			// Call ServiceTracker.getServices()
			// Should find testservice1
			os = st.getServices();
			assertThat(os).as("no TestService1").isNull();

			// Call ServiceTracker.size()
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 0").isZero();
		}
		finally {
			// Call ServiceTracker.close()
			st.close();

			tb4.uninstall();
			tb3.uninstall();
			tb2.uninstall();
			tb1.uninstall();
		}
	}

	@Test
	public void testFilterWithPropertyChanges() throws Exception {
		BundleContext context = this.context;
		// 2.23.5 Testcase5 (tc5), filter match
		// Tb1 contains service: testservice1
		// Call BundleContext.BundleContext.createFilter(String) (testservice1)

		Filter f = context.createFilter("(name=TestService1)");
		// Creates ServiceTracker object with Filter
		ServiceTracker<TestService1, TestService1> st = new ServiceTracker<TestService1, TestService1>(
				context, f, null);
		// Call ServiceTracker.open()
		st.open();
		try {
			// Call ServiceTracker.size()
			// Should reply 0
			assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 0").isZero();
			// Install tb1
			Bundle tb1 = installer.installBundle("tb1.jar");
			try {
				// Call ServiceTracker.size()
				// Should reply 1
				assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 1").isOne();
				// Call ServiceTracker.getServiceReferences()
				// Should find all
				// Call ServiceTracker.getServiceReferences()
				// Should find tb1, tb3
				ServiceReference<TestService1>[] srs = st
						.getServiceReferences();

				assertThat(srs).as("There were no ServiceReferences in this ServiceTracker.").isNotNull();
				for (int i = 0; i < srs.length; i++) {
					assertThat(srs[i].getProperty("name")).as("The ServiceReferences contains: TestService1").isEqualTo("TestService1");
				}

				// Change property for TestService1 so that the filter doesn't
				// match
				// The only way to change property is to have the
				// ServiceRegistration,
				// i.e. reg a new TestService1
				Hashtable<String, Object> ts1Props = new Hashtable<String, Object>();
				ts1Props.put("name", "TestService1");
				ts1Props.put("version", Float.valueOf(1.0f));
				ts1Props.put("compatible", Float.valueOf(1.0f));
				ts1Props.put("description", "TestService 1 in tbc");

				ServiceRegistration<TestService1> tsr1 = context
						.registerService(TestService1.class, new TestService1() {
							// empty
						}, ts1Props);

				assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 2").isEqualTo(2);
				ts1Props.put("name", "TestService1a");
				tsr1.setProperties(ts1Props);
				// Check that the servicetracker doesn't find the TestService
				assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 1").isOne();
				// Change property for tb1 so that the filter match again
				ts1Props.put("name", "TestService1");
				tsr1.setProperties(ts1Props);
				// Check that the servicetracker find TestService
				assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 2").isEqualTo(2);

				tsr1.unregister();
				assertThat(st.size()).as("The number of Services being tracked by ServiceTracker 1 is: 1").isOne();
			}
			finally {
				tb1.uninstall();
			}
		}
		finally {
			// Call ServiceTracker.close()
			st.close();
		}
	}

	@Test
	public void testTrackingCount() throws Exception {
		BundleContext context = this.context;
		ServiceTracker<TestService3,TestService3> st = new ServiceTracker<>(
				context, TestService3.class, null);
		assertThat(st
				.getTrackingCount()).as("ServiceTracker.getTrackingCount() == -1").isEqualTo(-1);
		st.open();
		try {
			// Should be 0
			assertThat(st
					.getTrackingCount()).as("ServiceTracker.getTrackingCount() == 0").isZero();

			ServiceRegistration<TestService3> sr = context
					.registerService(TestService3.class, new TestService3() {
						// empty
					}, null);
			// Should be 1
			assertThat(+st
					.getTrackingCount()).as("ServiceTracker.getTrackingCount() == 1").isOne();

			sr.unregister();
			// Should be 2
			assertThat(st
					.getTrackingCount()).as("ServiceTracker.getTrackingCount() == 2").isEqualTo(2);
		}
		finally {
			st.close();
		}
		assertThat(st
				.getTrackingCount()).as("ServiceTracker.getTrackingCount() == -1").isEqualTo(-1);
	}

	@Test
	public void testServiceTracker01() {
		// simple ServiceTracker test
		Runnable runIt = new Service();
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(testMethodName, Boolean.TRUE);
		ServiceRegistration<Runnable> reg = context
				.registerService(Runnable.class, runIt, props);
		ServiceTracker<Runnable, ServiceReference<Runnable>> testTracker = null;
		try {
			final boolean[] results = new boolean[] {false, false, false};
			ServiceTrackerCustomizer<Runnable, ServiceReference<Runnable>> testCustomizer = new ServiceTrackerCustomizer<Runnable, ServiceReference<Runnable>>() {
				public ServiceReference<Runnable> addingService(
						ServiceReference<Runnable> reference) {
					results[0] = true;
					return reference;
				}

				public void modifiedService(
						ServiceReference<Runnable> reference,
						ServiceReference<Runnable> service) {
					results[1] = true;
				}

				public void removedService(
						ServiceReference<Runnable> reference,
						ServiceReference<Runnable> service) {
					results[2] = true;
				}
			};
			try {
				testTracker = new ServiceTracker<Runnable, ServiceReference<Runnable>>(
						context,
						FrameworkUtil
								.createFilter("(&(objectClass=java.lang.Runnable)("
										+ testMethodName + "=true))"),
						testCustomizer);
			}
			catch (InvalidSyntaxException e) {
				fail("filter error", e);
			}
			testTracker.open();
			assertThat(results[0]).as("Did not call addingService").isTrue();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

			// change props to still match
			props.put("testChangeProp", Boolean.FALSE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did not call modifiedService").isTrue();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

			// change props to no longer match
			props.put(testMethodName, Boolean.FALSE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did not call removedService").isTrue();
			clearResults(results);

			// change props to no longer match
			props.put("testChangeProp", Boolean.TRUE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

			// change props back to match
			props.put(testMethodName, Boolean.TRUE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did not call addingService").isTrue();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

		}
		finally {
			if (reg != null)
				reg.unregister();
			if (testTracker != null)
				testTracker.close();
		}
	}

	@Test
	public void testServiceTracker02() {
		// simple ServiceTracker test
		Runnable runIt = new Service();
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(testMethodName, Boolean.FALSE);
		ServiceRegistration<Runnable> reg = context
				.registerService(Runnable.class, runIt, props);
		ServiceTracker<Runnable, ServiceReference<Runnable>> testTracker = null;
		try {
			final boolean[] results = new boolean[] {false, false, false};
			ServiceTrackerCustomizer<Runnable, ServiceReference<Runnable>> testCustomizer = new ServiceTrackerCustomizer<Runnable, ServiceReference<Runnable>>() {
				public ServiceReference<Runnable> addingService(
						ServiceReference<Runnable> reference) {
					results[0] = true;
					return reference;
				}

				public void modifiedService(
						ServiceReference<Runnable> reference,
						ServiceReference<Runnable> service) {
					results[1] = true;
				}

				public void removedService(
						ServiceReference<Runnable> reference,
						ServiceReference<Runnable> service) {
					results[2] = true;
				}
			};
			try {
				testTracker = new ServiceTracker<Runnable, ServiceReference<Runnable>>(
						context,
						FrameworkUtil
								.createFilter("(&(objectClass=java.lang.Runnable)("
										+ testMethodName + "=true))"),
						testCustomizer);
			}
			catch (InvalidSyntaxException e) {
				fail("filter error", e);
			}
			testTracker.open();
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

			// change props to match
			props.put(testMethodName, Boolean.TRUE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did not call addingService").isTrue();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

			// change props to still match
			props.put("testChangeProp", Boolean.TRUE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did not call modifiedService").isTrue();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

			// change props to no longer match
			props.put(testMethodName, Boolean.FALSE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did not call removedService").isTrue();
			clearResults(results);

			// change props to no longer match
			props.put("testChangeProp", Boolean.FALSE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

		}
		finally {
			if (reg != null)
				reg.unregister();
			if (testTracker != null)
				testTracker.close();
		}
	}

	@Test
	public void testServiceTracker03() {
		// simple ServiceTracker test
		Runnable runIt = new Service();
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(testMethodName, Boolean.TRUE);
		ServiceRegistration<Runnable> reg = context
				.registerService(
				Runnable.class, runIt, props);
		ServiceTracker<Runnable, ServiceReference<Runnable>> testTracker = null;
		try {
			final boolean[] results = new boolean[] {false, false, false};
			ServiceTrackerCustomizer<Runnable, ServiceReference<Runnable>> testCustomizer = new ServiceTrackerCustomizer<Runnable, ServiceReference<Runnable>>() {
				public ServiceReference<Runnable> addingService(
						ServiceReference<Runnable> reference) {
					results[0] = true;
					return reference;
				}

				public void modifiedService(
						ServiceReference<Runnable> reference,
						ServiceReference<Runnable> service) {
					results[1] = true;
				}

				public void removedService(
						ServiceReference<Runnable> reference,
						ServiceReference<Runnable> service) {
					results[2] = true;
				}
			};
			try {
				testTracker = new ServiceTracker<Runnable, ServiceReference<Runnable>>(
						context,
						FrameworkUtil
						.createFilter("(&(objectclass=java.lang.Runnable)("
								+ testMethodName.toLowerCase() + "=true))"),
						testCustomizer);
			}
			catch (InvalidSyntaxException e) {
				fail("filter error", e);
			}
			testTracker.open();
			assertThat(results[0]).as("Did not call addingService").isTrue();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

			// change props to not match
			props.put(testMethodName, Boolean.FALSE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did not call removedService").isTrue();
			clearResults(results);

			// change props to match
			props.put(testMethodName, Boolean.TRUE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did not call addingService").isTrue();
			assertThat(results[1]).as("Did call modifiedService").isFalse();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);

			// change props to still match
			props.put("testChangeProp", Boolean.FALSE);
			reg.setProperties(props);
			assertThat(results[0]).as("Did call addingService").isFalse();
			assertThat(results[1]).as("Did not call modifiedService").isTrue();
			assertThat(results[2]).as("Did call removedService").isFalse();
			clearResults(results);
		}
		finally {
			if (reg != null)
				reg.unregister();
			if (testTracker != null)
				testTracker.close();
		}
	}

	@Test
	public void testModifiedRanking() {
		Runnable runIt = new Service();
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(testMethodName, Boolean.TRUE);
		props.put(Constants.SERVICE_RANKING, Integer.valueOf(15));
		ServiceRegistration<Runnable> reg1 = context
				.registerService(
				Runnable.class, runIt, props);
		props.put(Constants.SERVICE_RANKING, Integer.valueOf(10));
		ServiceRegistration<Runnable> reg2 = context
				.registerService(
				Runnable.class, runIt, props);
		ServiceTracker<Runnable, Runnable> testTracker = null;
		try {
			try {
				testTracker = new ServiceTracker<Runnable, Runnable>(
						context,
						FrameworkUtil
						.createFilter("(&(objectclass=java.lang.Runnable)("
								+ testMethodName.toLowerCase() + "=true))"), null);
			}
			catch (InvalidSyntaxException e) {
				fail("filter error", e);
			}
			testTracker.open();
			assertThat(testTracker.getServiceReference()).as("wrong service reference").isEqualTo(reg1.getReference());

			props.put(Constants.SERVICE_RANKING, Integer.valueOf(20));
			reg2.setProperties(props);
			assertThat(testTracker.getServiceReference()).as("wrong service reference").isEqualTo(reg2.getReference());
		}
		finally {
			if (reg1 != null)
				reg1.unregister();
			if (reg2 != null)
				reg2.unregister();
			if (testTracker != null)
				testTracker.close();
		}
	}

	@Test
	public void testMap() {
		Service runIt = new Service();
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(testMethodName, Boolean.TRUE);
		props.put(Constants.SERVICE_RANKING, Integer.valueOf(15));
		ServiceRegistration<Service> reg1 = context.registerService(
				Service.class, runIt, props);
		props.put(Constants.SERVICE_RANKING, Integer.valueOf(10));
		ServiceRegistration<Service> reg2 = context.registerService(
				Service.class, runIt, props);
		ServiceTracker<Service, Service> testTracker = null;
		try {
			testTracker = new ServiceTracker<Service, Service>(context,
					Service.class, null);
			Map<ServiceReference<Service>, Service> map = testTracker
					.getTracked();
			assertThat(map).as("wrong size").hasSize(testTracker.size());
			testTracker.open();

			SortedMap<ServiceReference<Service>, Service> sortedMap = testTracker
					.getTracked();
			assertThat(sortedMap.firstKey()).as("wrong service reference").isEqualTo(reg1.getReference());
			assertThat(sortedMap.lastKey()).as("wrong service reference").isEqualTo(reg2.getReference());
			assertThat(sortedMap).as("wrong size").hasSize(testTracker.size());

			props.put(Constants.SERVICE_RANKING, Integer.valueOf(20));
			reg2.setProperties(props);

			sortedMap = testTracker.getTracked();
			assertThat(sortedMap.firstKey()).as("wrong service reference").isEqualTo(reg2.getReference());
			assertThat(sortedMap.lastKey()).as("wrong service reference").isEqualTo(reg1.getReference());
			assertThat(sortedMap).as("wrong size").hasSize(testTracker.size());
		}
		finally {
			if (reg1 != null)
				reg1.unregister();
			if (reg2 != null)
				reg2.unregister();
			if (testTracker != null)
				testTracker.close();
		}
	}

	private void clearResults(boolean[] results) {
		for (int i = 0; i < results.length; i++)
			results[i] = false;
	}

	/**
     * Similarly, a new type of SeviceTracker is added (AllServiceTracker). The
     * AllServiceTracker allows a bundle to track all services regardless of
     * what version of a package they are wired to. The AllServiceTracker is
     * identical in function to the ServiceTracker API except it will register
     * an AllServiceListener to track ServiceReference objects for a bundle.
     *
     * @spec ServiceTracker.open(boolean)
     * @throws Exception if there is any problem or an assert fails
     */
    @Test
    public void testAllServiceTracker01() throws Exception {
    	Bundle tb1;
        Bundle tb5;
		ServiceTracker<TestService1,TestService1> serviceTracker;

        tb1 = installer.installBundle("tb1.jar");
        tb5 = installer.installBundle("tb5.jar");

		serviceTracker = new ServiceTracker<>(
    			context,
				TestService1.class,
    			null);
    	serviceTracker.open(true);

    	try {
            assertThat(serviceTracker.size()).as("ServiceTracker must track services which are not class loader accessibile").isEqualTo(2);
            serviceTracker.close();

            serviceTracker.open(false);

			assertThat(serviceTracker.size()).as("ServiceTracker must track services which are not class loader accessibile").isOne();

    	}
    	finally {
            serviceTracker.close();
            tb5.uninstall();
    		tb1.uninstall();
    	}
    }

    /**
	 * Make sure different ServiceTrackers in same bundle get same service
	 * objects for a prototype scoped service.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPrototypeService() throws Exception {
		Bundle tb6 = installer.installBundle("tb6.jar");

		ServiceTracker<TestService1, TestService1> serviceTracker1 = new ServiceTracker<>(context,
				TestService1.class, null);
		ServiceTracker<TestService1, TestService1> serviceTracker2 = new ServiceTracker<>(context,
				TestService1.class, null);
		serviceTracker1.open();
		serviceTracker2.open();

		try {
			assertThat(serviceTracker1.size()).as("wrong size").isOne();
			assertThat(serviceTracker2.size()).as("wrong size").isOne();
			TestService1 s1 = serviceTracker1.getService();
			TestService1 s2 = serviceTracker2.getService();
			assertThat(s2).as("not same service").isSameAs(s1);
		} finally {
			serviceTracker1.close();
			serviceTracker2.close();
			tb6.uninstall();
		}
	}

	static class Service implements Runnable {
		public void run() {
			// nothing
		}
	};
}
