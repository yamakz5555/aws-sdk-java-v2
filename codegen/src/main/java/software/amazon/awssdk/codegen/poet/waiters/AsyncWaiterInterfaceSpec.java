/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.codegen.poet.waiters;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import java.util.concurrent.CompletableFuture;
import software.amazon.awssdk.codegen.model.intermediate.IntermediateModel;
import software.amazon.awssdk.codegen.model.intermediate.OperationModel;
import software.amazon.awssdk.codegen.poet.PoetExtensions;
import software.amazon.awssdk.core.waiters.WaiterResponse;

public final class AsyncWaiterInterfaceSpec extends BaseWaiterInterfaceSpec {

    private final IntermediateModel model;
    private final PoetExtensions poetExtensions;
    private final ClassName className;
    private final String modelPackage;

    public AsyncWaiterInterfaceSpec(IntermediateModel model) {
        super(model);
        this.modelPackage = model.getMetadata().getFullModelPackageName();
        this.model = model;
        this.poetExtensions = new PoetExtensions(model);
        this.className = poetExtensions.getAsyncWaiterInterface();
    }

    @Override
    protected ClassName clientClassName() {
        return poetExtensions.getClientClass(model.getMetadata().getAsyncInterface());
    }

    @Override
    public ClassName className() {
        return className;
    }

    @Override
    protected ParameterizedTypeName getWaiterResponseType(OperationModel opModel) {
        ClassName pojoResponse = ClassName.get(modelPackage, opModel.getReturnType().getReturnType());
        ParameterizedTypeName waiterResponse = ParameterizedTypeName.get(ClassName.get(WaiterResponse.class), pojoResponse);

        return ParameterizedTypeName.get(ClassName.get(CompletableFuture.class),
                                         waiterResponse);
    }
}