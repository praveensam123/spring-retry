/*
 * Copyright 2006-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.retry.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.retry.RetryContext;

public class NeverRetryPolicyTests {

	@Test
	public void testSimpleOperations() {
		NeverRetryPolicy policy = new NeverRetryPolicy();
		RetryContext context = policy.open(null);
		assertNotNull(context);
		// We can retry until the first exception is registered...
		assertTrue(policy.canRetry(context));
		assertTrue(policy.canRetry(context));
		policy.registerThrowable(context, null);
		assertFalse(policy.canRetry(context));
		policy.close(context);
		assertFalse(policy.canRetry(context));
	}

	@Test
	public void testRetryCount() {
		NeverRetryPolicy policy = new NeverRetryPolicy();
		RetryContext context = policy.open(null);
		assertNotNull(context);
		policy.registerThrowable(context, null);
		assertEquals(0, context.getRetryCount());
		policy.registerThrowable(context, new RuntimeException("foo"));
		assertEquals(1, context.getRetryCount());
		assertEquals("foo", context.getLastThrowable().getMessage());
	}

	@Test
	public void testParent() {
		NeverRetryPolicy policy = new NeverRetryPolicy();
		RetryContext context = policy.open(null);
		RetryContext child = policy.open(context);
		assertNotSame(child, context);
		assertSame(context, child.getParent());
	}

}
