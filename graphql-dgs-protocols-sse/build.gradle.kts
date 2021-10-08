dependencies {
    implementation("com.netflix.graphql.dgs:graphql-dgs")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-websocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("io.projectreactor:reactor-core")
    testImplementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
}