package com.example.barcodescannerdemo.database

import com.example.barcodescannerdemo.miscellaneous.BaseColumns

class VauxDataContract private constructor() {

    // classes in Kotlin are final, unless prefixed with 'open'

    class Vaux: BaseColumns(){

        companion object Vaux: BaseColumns() {
            const val TABLE_NAME = "vauxes_serial_numbers_for_locations"
            const val COLUMN_NAME_PLUS_CODE = "plus_code"
            const val COLUMN_NAME_LATITUDE = "latitude"
            const val COLUMN_NAME_LONGITUDE = "longitude"
            const val COLUMN_NAME_NUM_PLATEAUS = "num_plateaus"
            const val COLUMN_NAME_IS_ATTACHED = "is_attached"

        }

    }

    class Pallet: BaseColumns(){

        companion object Pallet: BaseColumns() {
            const val TABLE_NAME = "vauxes_pallets_coordinates"
            const val COLUMN_NAME_VAUX_SERIAL_NUMBER = "vaux_serial_number"
            const val COLUMN_NAME_LATITUDE = "latitude"
            const val COLUMN_NAME_LONGITUDE = "longitude"
            const val FOREIGN_KEY_CONSTRAINT_VAUX_SERIAL_NUMBER = "vaux_serial_number_pallets_fk"
        }

    }

    class Plateaus: BaseColumns(){

        companion object Plateaus: BaseColumns() {
            const val TABLE_NAME = "y_axis_height_for_vaux_plateaus"
            const val COLUMN_NAME_VAUX_SERIAL_NUMBER = "vaux_serial_number"
            const val COLUMN_NAME_HEIGHT = "height"
            const val FOREIGN_KEY_CONSTRAINT_VAUX_SERIAL_NUMBER = "vaux_serial_number_plateaus_fk"

        }

    }

    class TotalNumberOfVauxes: BaseColumns(){

        companion object TotalNumberOfVauxes: BaseColumns() {
            const val VIEW_NAME = "total_number_vauxes"
        }
    }

    class TotalVauxesForLocation: BaseColumns(){

        companion object TotalVauxesForLocation: BaseColumns() {
            const val VIEW_NAME = "number_of_vauxes_in_locations"
            const val COLUMN_NAME_NUM_VAUXES = "num_vauxes"

        }
    }

    /*

    - please note that the primary key columns within these data contracts are not noted here.
      This is because the '_ID' column from the 'BaseColumns' serves as the primary key column
      for all tables within the database

    */

}