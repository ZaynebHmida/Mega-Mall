<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateBoutiquesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('boutiques', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->integer('boutowner');
            $table->string('iconurl');
            $table->string('libelle');
            $table->string('adresse');
            $table->string('mail');
            $table->integer('tel');
            $table->string('description');
            $table->double('longitude');
            $table->double('latitude');



            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('boutiques');
    }
}
