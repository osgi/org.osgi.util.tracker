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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectBundleInstaller;
import org.osgi.test.common.install.BundleInstaller;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class BundleTrackerTests {

	@InjectBundleContext
	BundleContext		context;

	@InjectBundleInstaller
	BundleInstaller		installer;

	@Test
	public void testOpenClose() {
		BundleTracker<Bundle> bt = new BundleTracker<Bundle>(context,
				Bundle.ACTIVE, null);
		int size = bt.size();
		assertThat(size).as("size not zero").isZero();
		Bundle[] bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();

		bt.open();
		try {
			size = bt.size();
			assertThat(0 < size).as("size zero").isTrue();
			bundles = bt.getBundles();
			assertThat(bundles).as("getBundles() null").isNotNull();
			assertThat(bundles.length).as("size() != getBundles().length").isEqualTo(size);
			for (int i = 0, l = bundles.length; i < l; i++) {
				Bundle bundle = bundles[i];
				Object tracked = bt.getObject(bundle);
				assertThat(tracked).as("default tracked != bundle").isEqualTo(bundle);
			}
			Map<Bundle, Bundle> map = bt.getTracked();
			assertThat(map).as("size() != map.size()").hasSize(size);
			for (Map.Entry<Bundle, Bundle> e : map.entrySet()) {
				Bundle bundle = e.getKey();
				Object tracked = e.getValue();
				assertThat(tracked).as("default tracked != bundle").isEqualTo(bundle);
			}
		}
		finally {
			bt.close();
		}
		size = bt.size();
		assertThat(size).as("size not zero").isZero();
		bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();
		Map<Bundle, Bundle> map = bt.getTracked();
		assertThat(map).as("size() != map.size()").hasSize(size);

		bt.open();
		try {
			size = bt.size();
			assertThat(0 < size).as("size zero").isTrue();
			bundles = bt.getBundles();
			assertThat(bundles).as("getBundles() null").isNotNull();
			assertThat(bundles.length).as("size() != getBundles().length").isEqualTo(size);
			for (int i = 0, l = bundles.length; i < l; i++) {
				Bundle bundle = bundles[i];
				Object tracked = bt.getObject(bundle);
				assertThat(tracked).as("default tracked != bundle").isEqualTo(bundle);
			}
			map = bt.getTracked();
			assertThat(map).as("size() != map.size()").hasSize(size);
			for (Map.Entry<Bundle, Bundle> e : map.entrySet()) {
				Bundle bundle = e.getKey();
				Object tracked = e.getValue();
				assertThat(tracked).as("default tracked != bundle").isEqualTo(bundle);
			}
		}
		finally {
			bt.close();
		}
		size = bt.size();
		assertThat(size).as("size not zero").isZero();
		bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();
		map = bt.getTracked();
		assertThat(map).as("size() != map.size()").hasSize(size);
	}

	// add testing for isEmpty
	@Test
	public void testOpenClose2() {
		BundleTracker<Bundle> bt = new BundleTracker<Bundle>(context,
				Bundle.ACTIVE, null);
		int size = bt.size();
		assertThat(size).as("size not zero").isZero();
		assertThat(bt.isEmpty()).as("not empty").isTrue();
		Bundle[] bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();

		bt.open();
		try {
			size = bt.size();
			assertThat(0 < size).as("size zero").isTrue();
			assertThat(bt.isEmpty()).as("empty").isFalse();
			bundles = bt.getBundles();
			assertThat(bundles).as("getBundles() null").isNotNull();
			assertThat(bundles.length).as("size() != getBundles().length").isEqualTo(size);
			for (int i = 0, l = bundles.length; i < l; i++) {
				Bundle bundle = bundles[i];
				Object tracked = bt.getObject(bundle);
				assertThat(tracked).as("default tracked != bundle").isEqualTo(bundle);
			}
			Map<Bundle, Bundle> map = bt.getTracked();
			assertThat(map).as("size() != map.size()").hasSize(size);
			for (Map.Entry<Bundle, Bundle> e : map.entrySet()) {
				Bundle bundle = e.getKey();
				Object tracked = e.getValue();
				assertThat(tracked).as("default tracked != bundle").isEqualTo(bundle);
			}
		}
		finally {
			bt.close();
		}
		size = bt.size();
		assertThat(size).as("size not zero").isZero();
		assertThat(bt.isEmpty()).as("not empty").isTrue();
		bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();
		Map<Bundle, Bundle> map = bt.getTracked();
		assertThat(map).as("size() != map.size()").hasSize(size);

		bt.open();
		try {
			size = bt.size();
			assertThat(0 < size).as("size zero").isTrue();
			assertThat(bt.isEmpty()).as("empty").isFalse();
			bundles = bt.getBundles();
			assertThat(bundles).as("getBundles() null").isNotNull();
			assertThat(bundles.length).as("size() != getBundles().length").isEqualTo(size);
			for (int i = 0, l = bundles.length; i < l; i++) {
				Bundle bundle = bundles[i];
				Object tracked = bt.getObject(bundle);
				assertThat(tracked).as("default tracked != bundle").isEqualTo(bundle);
			}
			map = bt.getTracked();
			assertThat(map).as("size() != map.size()").hasSize(size);
			for (Map.Entry<Bundle, Bundle> e : map.entrySet()) {
				Bundle bundle = e.getKey();
				Object tracked = e.getValue();
				assertThat(tracked).as("default tracked != bundle").isEqualTo(bundle);
			}
		}
		finally {
			bt.close();
		}
		size = bt.size();
		assertThat(size).as("size not zero").isZero();
		bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();
		map = bt.getTracked();
		assertThat(map).as("size() != map.size()").hasSize(size);
	}

	@Test
	public void testCustomizer() throws Exception {
		final boolean[] customizerCalled = new boolean[] {false, false, false};
		BundleTracker<BundleWrapper> bt = new BundleTracker<BundleWrapper>(
				context, Bundle.ACTIVE,
				new BundleTrackerCustomizer<BundleWrapper>() {

					public BundleWrapper addingBundle(Bundle bundle,
							BundleEvent event) {
						synchronized (customizerCalled) {
							customizerCalled[0] = true;
						}
						return new BundleWrapper(bundle);
					}

					public void modifiedBundle(Bundle bundle,
							BundleEvent event, BundleWrapper tracked) {
						synchronized (customizerCalled) {
							customizerCalled[1] = true;
						}
					}

					public void removedBundle(Bundle bundle, BundleEvent event,
							BundleWrapper tracked) {
						synchronized (customizerCalled) {
							customizerCalled[2] = true;
						}
					}

				});
		testCustomizerInternal(bt, customizerCalled);
	}

	@Test
	public void testSubclass() throws Exception {
		final boolean[] customizerCalled = new boolean[] {false, false, false};
		BundleTracker<BundleWrapper> bt = new BundleTracker<BundleWrapper>(
				context, Bundle.ACTIVE, null) {

			public BundleWrapper addingBundle(Bundle bundle, BundleEvent event) {
				synchronized (customizerCalled) {
					customizerCalled[0] = true;
				}
				return new BundleWrapper(bundle);
			}

			public void modifiedBundle(Bundle bundle, BundleEvent event,
					BundleWrapper tracked) {
				synchronized (customizerCalled) {
					customizerCalled[1] = true;
				}
			}

			public void removedBundle(Bundle bundle, BundleEvent event,
					BundleWrapper tracked) {
				synchronized (customizerCalled) {
					customizerCalled[2] = true;
				}
			}
		};
		testCustomizerInternal(bt, customizerCalled);
	}

	private void testCustomizerInternal(BundleTracker<BundleWrapper> bt,
			boolean[] customizerCalled) throws Exception {
		synchronized (customizerCalled) {
			assertThat(customizerCalled[0]).as("adding called").isFalse();
			assertThat(customizerCalled[1]).as("modified called").isFalse();
			assertThat(customizerCalled[2]).as("removed called").isFalse();
		}

		Bundle[] bundles;
		bt.open();
		try {
			bundles = bt.getBundles();
			assertThat(bundles).as("getBundles() null").isNotNull();
			for (int i = 0, l = bundles.length; i < l; i++) {
				Bundle bundle = bundles[i];
				BundleWrapper tracked = bt.getObject(bundle);
				assertThat(tracked
						.getBundle()).as("tracked.getBundle() != bundle").isEqualTo(bundle);
			}
			Map<Bundle, BundleWrapper> map = bt.getTracked();
			assertThat(map).as("size() != map.size()").hasSize(bt.size());
			for (Map.Entry<Bundle, BundleWrapper> e : map.entrySet()) {
				Bundle bundle = e.getKey();
				BundleWrapper tracked = e.getValue();
				assertThat(tracked
						.getBundle()).as("default tracked != bundle").isEqualTo(bundle);
			}
			synchronized (customizerCalled) {
				assertThat(customizerCalled[0]).as("adding not called").isTrue();
				customizerCalled[0] = false;
				assertThat(customizerCalled[1]).as("modified called").isFalse();
				assertThat(customizerCalled[2]).as("removed called").isFalse();
			}
		}
		finally {
			bt.close();
		}
		bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();
		synchronized (customizerCalled) {
			assertThat(customizerCalled[0]).as("adding called").isFalse();
			assertThat(customizerCalled[1]).as("modified called").isFalse();
			assertThat(customizerCalled[2]).as("removed not called").isTrue();
			customizerCalled[2] = false;
		}

		bt.open();
		try {
			bundles = bt.getBundles();
			assertThat(bundles).as("getBundles() null").isNotNull();
			for (int i = 0, l = bundles.length; i < l; i++) {
				Bundle bundle = bundles[i];
				BundleWrapper tracked = bt.getObject(bundle);
				assertThat(tracked
						.getBundle()).as("tracked.getBundle() != bundle").isEqualTo(bundle);
			}
			Map<Bundle, BundleWrapper> map = bt.getTracked();
			assertThat(map).as("size() != map.size()").hasSize(bt.size());
			for (Map.Entry<Bundle, BundleWrapper> e : map.entrySet()) {
				Bundle bundle = e.getKey();
				BundleWrapper tracked = e.getValue();
				assertThat(tracked
						.getBundle()).as("default tracked != bundle").isEqualTo(bundle);
			}
			synchronized (customizerCalled) {
				assertThat(customizerCalled[0]).as("adding not called").isTrue();
				customizerCalled[0] = false;
				assertThat(customizerCalled[1]).as("modified called").isFalse();
				assertThat(customizerCalled[2]).as("removed called").isFalse();
			}

			Bundle tb1 = installer.installBundle("tb1.jar", false);
			try {
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding called").isFalse();
					assertThat(customizerCalled[1]).as("modified called").isFalse();
					assertThat(customizerCalled[2]).as("removed called").isFalse();
				}
				tb1.start();
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding not called").isTrue();
					customizerCalled[0] = false;
					assertThat(customizerCalled[1]).as("modified called").isFalse();
					assertThat(customizerCalled[2]).as("removed called").isFalse();
				}
				tb1.stop();
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding called").isFalse();
					assertThat(customizerCalled[1]).as("modified called").isFalse();
					assertThat(customizerCalled[2]).as("removed not called").isTrue();
					customizerCalled[2] = false;
				}
				tb1.start();
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding not called").isTrue();
					customizerCalled[0] = false;
					assertThat(customizerCalled[1]).as("modified called").isFalse();
					assertThat(customizerCalled[2]).as("removed called").isFalse();
				}
				bt.remove(tb1);
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding called").isFalse();
					assertThat(customizerCalled[1]).as("modified called").isFalse();
					assertThat(customizerCalled[2]).as("removed not called").isTrue();
					customizerCalled[2] = false;
				}
			}
			finally {
				tb1.uninstall();
			}
		}
		finally {
			bt.close();
		}
		bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();
		synchronized (customizerCalled) {
			assertThat(customizerCalled[0]).as("adding called").isFalse();
			assertThat(customizerCalled[1]).as("modified called").isFalse();
			assertThat(customizerCalled[2]).as("removed not called").isTrue();
			customizerCalled[2] = false;
		}
	}

	@Test
	public void testTrackingCount() {
		BundleTracker<Bundle> bt = new BundleTracker<Bundle>(context,
				Bundle.ACTIVE, null);
		assertThat(bt.size()).as("size not zero").isZero();
		assertThat(bt.getTrackingCount()).as("tracking count not -1").isEqualTo(-1);
		bt.open();
		try {
			assertThat(0 < bt.size()).as("size zero").isTrue();
			int trackingCount = bt.getTrackingCount();
			assertThat(0 < trackingCount).as("tracking count zero").isTrue();
			Bundle[] bundles = bt.getBundles();
			for (int i = 0, l = bundles.length; i < l; i++, trackingCount = bt
					.getTrackingCount()) {
				bt.remove(bundles[i]);
				assertThat(trackingCount < bt
						.getTrackingCount()).as("tracking count not incremented").isTrue();
			}
			assertThat(bt.size()).as("size not zero").isZero();
		}
		finally {
			bt.close();
		}
		assertThat(bt.getTrackingCount()).as("tracking count not -1").isEqualTo(-1);
	}

	@Test
	public void testEvents() throws Exception {
		Bundle[] bundles = context.getBundles();
		final List<Bundle> extraneousBundles = new ArrayList<Bundle>(
				bundles.length);
		for (Bundle b : bundles) {
			if ((b.getState() & (Bundle.INSTALLED | Bundle.RESOLVED)) != 0) {
				extraneousBundles.add(b);
			}
		}
		final BundleEvent[] events = new BundleEvent[] {null, null, null};
		BundleTracker<Bundle> bt = new BundleTracker<Bundle>(context,
				Bundle.INSTALLED | Bundle.RESOLVED, null) {

			public Bundle addingBundle(Bundle bundle, BundleEvent event) {
				if (extraneousBundles.contains(bundle)) {
					return null;
				}
				synchronized (events) {
					events[0] = event;
				}
				return bundle;
			}

			public void modifiedBundle(Bundle bundle, BundleEvent event,
					Bundle tracked) {
				synchronized (events) {
					events[1] = event;
				}
			}

			public void removedBundle(Bundle bundle, BundleEvent event,
					Bundle tracked) {
				synchronized (events) {
					events[2] = event;
				}
			}
		};
		synchronized (events) {
			assertThat(events[0]).as("adding called").isNull();
			assertThat(events[1]).as("modified called").isNull();
			assertThat(events[2]).as("removed called").isNull();
		}
		bt.open();
		try {
			bundles = bt.getBundles();
			assertThat(bundles).as("getBundles() not null").isNull();
			synchronized (events) {
				assertThat(events[0]).as("adding called").isNull();
				assertThat(events[1]).as("modified called").isNull();
				assertThat(events[2]).as("removed called").isNull();
			}
			Bundle tb1 = installer.installBundle("tb1.jar", false);
			try {
				synchronized (events) {
					assertThat(events[0]).as("adding not called").isNotNull();
					assertThat(events[0].getType()).as("event type not installed").isEqualTo(BundleEvent.INSTALLED);
					assertThat(events[0]
							.getBundle()).as("event bundle not tb1").isEqualTo(tb1);
					events[0] = null;
					assertThat(events[1]).as("modified called").isNull();
					assertThat(events[2]).as("removed called").isNull();
				}

				tb1.start();
				synchronized (events) {
					assertThat(events[0]).as("adding called").isNull();
					assertThat(events[1]).as("modified not called").isNotNull();
					assertThat(events[1].getType()).as("event type not resolved").isEqualTo(BundleEvent.RESOLVED);
					assertThat(events[1]
							.getBundle()).as("event bundle not tb1").isEqualTo(tb1);
					events[1] = null;
					assertThat(events[2]).as("removed not called").isNotNull();
					assertThat(events[2].getType()).as("event type not started").isEqualTo(BundleEvent.STARTING);
					assertThat(events[2]
							.getBundle()).as("event bundle not tb1").isEqualTo(tb1);
					events[2] = null;
				}

				tb1.stop();
				synchronized (events) {
					assertThat(events[0]).as("adding not called").isNotNull();
					assertThat(events[0].getType()).as("event type not installed").isEqualTo(BundleEvent.STOPPED);
					assertThat(events[0]
							.getBundle()).as("event bundle not tb1").isEqualTo(tb1);
					events[0] = null;
					assertThat(events[1]).as("modified called").isNull();
					assertThat(events[2]).as("removed called").isNull();
				}
			}
			finally {
				tb1.uninstall();
			}
			synchronized (events) {
				assertThat(events[0]).as("adding called").isNull();
				assertThat(events[1]).as("modified not called").isNotNull();
				assertThat(events[1].getType()).as("event type not unresolved").isEqualTo(BundleEvent.UNRESOLVED);
				assertThat(events[1].getBundle()).as("event bundle not tb1").isEqualTo(tb1);
				events[1] = null;
				assertThat(events[2]).as("removed not called").isNotNull();
				assertThat(events[2].getType()).as("event type not uninstalled").isEqualTo(BundleEvent.UNINSTALLED);
				assertThat(events[2].getBundle()).as("event bundle not tb1").isEqualTo(tb1);
				events[2] = null;
			}
		}
		finally {
			bt.close();
		}
		bundles = bt.getBundles();
		assertThat(bundles).as("getBundles() not null").isNull();
		synchronized (events) {
			assertThat(events[0]).as("adding called").isNull();
			assertThat(events[1]).as("modified called").isNull();
			assertThat(events[2]).as("removed called").isNull();
		}
	}

	@Test
	public void testModified() throws Exception {
		final boolean[] customizerCalled = new boolean[] {false, false, false};
		BundleTracker<BundleWrapper> bt = new BundleTracker<BundleWrapper>(
				context, Bundle.INSTALLED | Bundle.RESOLVED
						| Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING,
				new BundleTrackerCustomizer<BundleWrapper>() {

					public BundleWrapper addingBundle(Bundle bundle,
							BundleEvent event) {
						synchronized (customizerCalled) {
							customizerCalled[0] = true;
						}
						return new BundleWrapper(bundle);
					}

					public void modifiedBundle(Bundle bundle,
							BundleEvent event, BundleWrapper tracked) {
						synchronized (customizerCalled) {
							customizerCalled[1] = true;
						}
					}

					public void removedBundle(Bundle bundle, BundleEvent event,
							BundleWrapper tracked) {
						synchronized (customizerCalled) {
							customizerCalled[2] = true;
						}
					}

				});

		synchronized (customizerCalled) {
			assertThat(customizerCalled[0]).as("adding called").isFalse();
			assertThat(customizerCalled[1]).as("modified called").isFalse();
			assertThat(customizerCalled[2]).as("removed called").isFalse();
		}

		Bundle[] bundles;
		bt.open();
		try {
			bundles = bt.getBundles();
			assertThat(bundles).as("getBundles() null").isNotNull();
			for (int i = 0, l = bundles.length; i < l; i++) {
				Bundle bundle = bundles[i];
				BundleWrapper tracked = bt.getObject(bundle);
				assertThat(tracked.getBundle()).as("tracked.getBundle() != bundle").isEqualTo(bundle);
			}
			Map<Bundle, BundleWrapper> map = bt.getTracked();
			assertThat(map).as("size() != map.size()").hasSize(bt.size());
			for (Map.Entry<Bundle, BundleWrapper> e : map.entrySet()) {
				Bundle bundle = e.getKey();
				BundleWrapper tracked = e.getValue();
				assertThat(tracked.getBundle()).as("default tracked != bundle").isEqualTo(bundle);
			}
			synchronized (customizerCalled) {
				assertThat(customizerCalled[0]).as("adding not called").isTrue();
				customizerCalled[0] = false;
				assertThat(customizerCalled[1]).as("modified called").isFalse();
				assertThat(customizerCalled[2]).as("removed called").isFalse();
			}

			Bundle tb1 = installer.installBundle("tb1.jar", false);
			try {
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding called").isTrue();
					customizerCalled[0] = false;
					assertThat(customizerCalled[1]).as("modified called").isFalse();
					assertThat(customizerCalled[2]).as("removed called").isFalse();
				}
				context.getBundle(0).adapt(FrameworkWiring.class).resolveBundles(Arrays.asList(tb1));
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding not called").isFalse();
					assertThat(customizerCalled[1]).as("modified called").isTrue();
					customizerCalled[1] = false;
					assertThat(customizerCalled[2]).as("removed called").isFalse();
				}
				tb1.start();
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding not called").isFalse();
					assertThat(customizerCalled[1]).as("modified called").isTrue();
					customizerCalled[1] = false;
					assertThat(customizerCalled[2]).as("removed called").isFalse();
				}
				tb1.stop();
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding not called").isFalse();
					assertThat(customizerCalled[1]).as("modified called").isTrue();
					customizerCalled[1] = false;
					assertThat(customizerCalled[2]).as("removed called").isFalse();
				}
				tb1.start();
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding not called").isFalse();
					assertThat(customizerCalled[1]).as("modified called").isTrue();
					customizerCalled[1] = false;
					assertThat(customizerCalled[2]).as("removed called").isFalse();
				}
				bt.remove(tb1);
				synchronized (customizerCalled) {
					assertThat(customizerCalled[0]).as("adding called").isFalse();
					assertThat(customizerCalled[1]).as("modified called").isFalse();
					assertThat(customizerCalled[2]).as("removed not called").isTrue();
					customizerCalled[2] = false;
				}
			}
			finally {
				tb1.uninstall();
			}
		}
		finally {
			bt.close();
		}
	}

	static class BundleWrapper {
		private final Bundle	bundle;

		BundleWrapper(Bundle bundle) {
			this.bundle = bundle;
		}

		Bundle getBundle() {
			return bundle;
		}
	}
}
